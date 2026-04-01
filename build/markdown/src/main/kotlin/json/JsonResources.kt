package org.ornet.json

import kotlinx.serialization.json.Json
import org.ornet.Nomenclature
import org.ornet.PatEvent
import org.ornet.SdcLibrary
import org.ornet.SdcLibraryFeatures
import org.ornet.TestResult
import org.ornet.TestResults
import org.ornet.TestSequence
import java.io.File

class JsonResources(
    testSequenceFile: File,
    testResultsDir: File,
    sdcLibrariesDir: File,
    nomenclatureFile: File,
) {
    val json = Json { prettyPrint = true }

    val testSequence = testSequenceFile.let { file ->
        file.readText().let {
            json.decodeFromString<TestSequence>(it)
        }
    }

    val sdcLibraries = sdcLibrariesDir.let { dir ->
        dir.listFiles()!!.filter { it.extension.lowercase() == "json" }.map {
            json.decodeFromString<SdcLibrary>(it.readText())
        }
    }

    val sdcLibrariesPerPatEvent = testResultsDir.let { testResultsDir ->
        testResultsDir.listFiles()!!.filter { it.isDirectory }.associate { patEventDir ->
            val libs = File(patEventDir, "participants").listFiles()!!.filter { it.isFile }.map {
                json.decodeFromString<SdcLibraryFeatures>(it.readText())
            }
            patEventDir.name to libs
        }
    }

    val patEvents = testResultsDir.let { testResultsDir ->
        testResultsDir.listFiles()?.filter { it.isDirectory }?.map { patEventDir ->
            val metaFilename = "${patEventDir.name}.meta.json"
            val patEventFile = requireNotNull(
                patEventDir.listFiles()?.firstOrNull { it.name.lowercase() == metaFilename }
            ) {
                "'${patEventDir.absolutePath}' does not contain a metadata file"
            }

            val patEvent = json.decodeFromString<PatEvent>(patEventFile.readText())

            val testResults = patEventDir.listFiles()!!
                .filter { it.isFile }
                .filterNot { it.name.lowercase() == metaFilename }
                .associateWith { json.decodeFromString<TestResults>(it.readText()) }

            testResults.forEach {
                require(it.value.patNumber == patEvent.patNumber) {
                    "Found event mismatch between PAT number in '${patEventFile.absolutePath}' (${patEvent.patNumber}) and '${it.key.absolutePath}' (${it.value.patNumber})"
                }
            }

            mergeTestResults(patEvent, testResults.values.map { it.testResults }.flatten())
        } ?: emptyList()
    }

    val nomenclature = nomenclatureFile.let { file ->
        file.readText().let {
            json.decodeFromString<Nomenclature>(it)
        }
    }

    private companion object {
        // reads all files containing test results and merges them in a way that the resulting object contains
        // all results; duplicates will cause the merge to fail
        fun mergeTestResults(event: PatEvent, testResults: List<TestResult>): PatEvent {
            val all = event.testResults + testResults
            val allIds = all.map { res ->
                res.caseIds.map {
                    "${res.consumerLibraryId} -> ${res.providerLibraryId}: $it"
                }
            }.flatten()
            val distinctIds = allIds.distinct()
            val duplicates = allIds - distinctIds
            require(duplicates.isEmpty()) {
                "Found duplicated test results for \n\n${duplicates.joinToString("\n- ")}\n"
            }

            return event.copy(
                testResults = all
            )
        }
    }
}