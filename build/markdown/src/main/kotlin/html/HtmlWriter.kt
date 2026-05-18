package org.ornet.html

import org.ornet.Binding
import org.ornet.PatEvent
import org.ornet.SdcLibrary
import org.ornet.SdcLibraryFeatures
import org.ornet.TestSequence
import org.ornet.htmlFileNameInteropMatrix
import java.io.File

class HtmlWriter(
    private val testResultsRootDir: File,
    private val formsDir: File,
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

                val content = TestResultsHtmlExport.patEventHtml(
                    patEvent,
                    testSequence,
                    sdcLibraries,
                    sdcLibraryFeatures[outputDirectory.name] ?: emptyList(),
                    binding
                )

                val outputFilename = htmlFileNameInteropMatrix(patEvent, binding)

                val destinations = listOf(
                    File(testResultsRootDir, outputFilename),
                    File(outputDirectory, outputFilename)
                )

                destinations.forEach {
                    it.writeText(content)
                }
            }
        }
    }

    fun writeForms(
        patEvents: List<PatEvent>,
        testSequence: TestSequence,
        sdcLibraries: List<SdcLibrary>,
    ) {
        val outputDirectory = formsDir.also {
            it.mkdirs()
        }

        File(outputDirectory, "library-manifest-form.html").writeText(
            FormsHtmlExport.libraryManifestFormHtml(testSequence)
        )

        File(outputDirectory, "test-results-form.html").writeText(
            FormsHtmlExport.testResultsFormHtml(testSequence, patEvents, sdcLibraries)
        )
    }
}