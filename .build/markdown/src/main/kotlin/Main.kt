package org.ornet

import org.ornet.json.JsonResources
import org.ornet.markdown.MarkdownWriter
import java.io.File

const val RESOURCES_FOLDER = "../../resources"
const val DB_TEST_RESULTS_FOLDER = "$RESOURCES_FOLDER/test-results"
const val DB_SDC_LIBRARIES_FOLDER = "$RESOURCES_FOLDER/sdc-libraries"
const val DOCS_FOLDER = "../../docs"
const val DOCS_TEST_RESULTS_FOLDER = "$DOCS_FOLDER/test-results"
const val DOCS_TEST_SEQUENCE_FOLDER = "$DOCS_FOLDER/test-sequence"
const val DOCS_SDC_LIBRARIES_FOLDER = "$DOCS_FOLDER/sdc-libraries"
const val TEST_SEQUENCE = "test-sequence"

fun main() {
    // resolve input/output directories
    val dbDir = File(RESOURCES_FOLDER).also {
        require(it.exists()) { "Directory '$RESOURCES_FOLDER' not exists" }
    }
    val rootDir = File(DOCS_FOLDER).also {
        require(it.exists()) { "Directory '$RESOURCES_FOLDER' not exists" }
    }

    val testResultsDir = File(DOCS_TEST_RESULTS_FOLDER).also {
        it.mkdirs()
    }
    val testSequenceDir = File(DOCS_TEST_SEQUENCE_FOLDER).also {
        it.mkdirs()
    }
    val sdcLibrariesDir = File(DOCS_SDC_LIBRARIES_FOLDER).also {
        it.mkdirs()
    }

    // read resources
    val jsonResources = JsonResources(
        testSequenceFile = File(dbDir, "$TEST_SEQUENCE.json"),
        testResultsDir = File(DB_TEST_RESULTS_FOLDER),
        sdcLibrariesDir = File(DB_SDC_LIBRARIES_FOLDER)
    )

    // write markdown files for site generation
    MarkdownWriter(
        rootDir = rootDir,
        testResultsDir = testResultsDir,
        testSequenceDir = testSequenceDir,
        sdcLibrariesDir = sdcLibrariesDir
    ).let {
        it.writeTestSequence(
            testSequence = jsonResources.testSequence
        )

        it.writePatEvents(
            patEvents = jsonResources.patEvents,
            testSequence = jsonResources.testSequence,
            sdcLibraries = jsonResources.sdcLibraries
        )

        it.writeSdcLibraries(
            sdcLibraries = jsonResources.sdcLibraries,
            testSequence = jsonResources.testSequence
        )

        it.writeJsonTemplates(
            testSequence = jsonResources.testSequence
        )
    }
}

