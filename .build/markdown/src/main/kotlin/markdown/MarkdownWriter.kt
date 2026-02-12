package org.ornet.markdown

import org.ornet.PatEvent
import org.ornet.SdcLibrary
import org.ornet.TestSequence
import org.ornet.sanitizedPath
import java.io.File

// central access to write markdown files for static site generation
class MarkdownWriter(
    private val rootDir: File,
    private val testResultsDir: File,
    private val testSequenceDir: File,
    private val sdcLibrariesDir: File,
) {
    // write json templates
    fun writeJsonTemplates(
        testSequence: TestSequence,
    ) {
        File(rootDir, mdFilename("json-templates")).writeText(
            JsonTemplatesExport.jsonTemplatesMd(
                testSequence
            )
        )
    }

    // write index file and all test sequences
    fun writeTestSequence(
        testSequence: TestSequence,
    ) {
        File(testSequenceDir, INDEX_FILENAME).writeText(TestSequenceMarkdownExport.indexMd(testSequence))

        for (testGroup in testSequence.testGroups) {
            File(
                testSequenceDir,
                mdFilename(testGroup.name)
            ).writeText(TestSequenceMarkdownExport.testGroupMd(testGroup))
        }
    }

    // writes index file and all pat events
    fun writePatEvents(
        patEvents: List<PatEvent>,
        testSequence: TestSequence,
        sdcLibraries: List<SdcLibrary>,
    ) {
        File(testResultsDir, INDEX_FILENAME).writeText(TestResultsMarkdownExport.indexMd(patEvents))

        for (patEvent in patEvents) {
            val outputFilename = "pat-${patEvent.patNumber}"
            File(testResultsDir, mdFilename(outputFilename)).writeText(
                TestResultsMarkdownExport.patEventMd(patEvent, testSequence, sdcLibraries)
            )
        }
    }

    // writes index file and all libraries
    fun writeSdcLibraries(
        sdcLibraries: List<SdcLibrary>,
        testSequence: TestSequence,
    ) {
        File(sdcLibrariesDir, INDEX_FILENAME).writeText(SdcLibrariesMarkdownExport.indexMd(sdcLibraries))

        for (lib in sdcLibraries) {
            File(sdcLibrariesDir, mdFilename(lib.id)).writeText(
                SdcLibrariesMarkdownExport.sdcLibraryMd(lib, testSequence)
            )
        }
    }

    companion object {
        private const val INDEX_FILENAME = "index.md"

        fun mdFilename(name: String): String {
            return sanitizedPath(name) + ".md"
        }
    }
}

