package org.ornet.html

import org.ornet.Binding
import org.ornet.PatEvent
import org.ornet.SdcLibrary
import org.ornet.SdcLibraryFeatures
import org.ornet.TestSequence
import org.ornet.html.TestResultsHtmlExport
import org.ornet.htmlFileNameInteropMatrix
import java.io.File

class HtmlWriter(
    private val testResultsRootDir: File,
) {
    fun writePatEvents(
        patEvents: List<PatEvent>,
        testSequence: TestSequence,
        sdcLibraries: List<SdcLibrary>,
        sdcLibraryFeatures: Map<String, List<SdcLibraryFeatures>>,
    ) {
        for (patEvent in patEvents) {
            val outputDirectory = File(testResultsRootDir, "pat-${patEvent.patNumber}").also {
                it.mkdirs()
            }

            for (binding in Binding.entries) {
                if (sdcLibraryFeatures["pat-${patEvent.patNumber}"]?.none { binding.json in it.bindings } ?: true) {
                    continue
                }

                val outputFilename = htmlFileNameInteropMatrix(patEvent, binding)
                File(outputDirectory, outputFilename).writeText(
                    TestResultsHtmlExport.patEventHtml(
                        patEvent,
                        testSequence,
                        sdcLibraries,
                        sdcLibraryFeatures[outputDirectory.name] ?: emptyList(),
                        binding
                    )
                )
            }
        }
    }
}