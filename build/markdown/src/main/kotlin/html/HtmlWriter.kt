package org.ornet.html

import org.ornet.PatEvent
import org.ornet.SdcLibrary
import org.ornet.SdcLibraryFeatures
import org.ornet.TestSequence
import org.ornet.html.TestResultsHtmlExport
import java.io.File

class HtmlWriter(
    private val testResultsRootDir: File,
) {
    fun writePatEvents(
        patEvents: List<PatEvent>,
        testSequence: TestSequence,
        sdcLibraries: List<SdcLibrary>,
        sdcLibraryFeatures: Map<String, List<SdcLibraryFeatures>>
    ) {
        for (patEvent in patEvents) {
            val outputDirectory = File(testResultsRootDir, "pat-${patEvent.patNumber}").also {
                it.mkdirs()
            }

            val outputFilename = "print.html"
            File(outputDirectory, outputFilename).writeText(
                TestResultsHtmlExport.patEventHtml(
                    patEvent,
                    testSequence,
                    sdcLibraries,
                    sdcLibraryFeatures[outputDirectory.name] ?: emptyList()
                )
            )
        }
    }
}