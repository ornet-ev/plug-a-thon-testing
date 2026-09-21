package org.ornet.markdown

import org.ornet.Binding
import org.ornet.InteroperabilityMatrix
import org.ornet.PatEvent
import org.ornet.SdcLibrary
import org.ornet.SdcLibraryFeatures
import org.ornet.TestSequence
import org.ornet.Verdict
import org.ornet.createInteroperabilityMatrix
import org.ornet.htmlFileNameInteropMatrix
import org.ornet.sortAndConcatenate


object TestResultsMarkdownExport {
    fun indexMd(src: List<PatEvent>): String {
        val list = src.joinToString("") {
            Markdown.generate {
                listItem(
                    link(
                        "pat-${it.patNumber}",
                        "PAT#${it.patNumber}"
                    )
                )
            }
        }

        return Markdown.generate {
            """
                ${frontMatter("icon" to "lucide/list-checks")}
                # Test Results
                
                $list
            """
        }
    }

    fun patEventMd(
        src: PatEvent,
        testSequence: TestSequence,
        libraries: List<SdcLibrary>,
        libFeatures: List<SdcLibraryFeatures>,
    ): String {
        val bindings = libFeatures
            .map { it.bindings }
            .flatten()
            .toSet()
            .map { Binding.fromJson(it) }

        val interopMatrix = createInteroperabilityMatrix(
            src,
            testSequence,
            libraries,
            libFeatures
        )

        val libNames = libraries.associate { it.id to it.name }

        val header = Markdown.generate {
            val icon = "icon" to "lucide/circle-check-big"
            val hideSidebars = "hide" to frontMatterListValues("navigation", "toc")
            """
                ${frontMatter(icon, hideSidebars)}

                ${heading("PAT#${src.patNumber}")}
            """
        }

        val legend = Markdown.generate(false) {
            """
??? Legend
    - :lucide-circle-check: featured tests succeeded
    - :lucide-circle-x: featured tests failed
    - :lucide-circle-question-mark: missing test results
    - :lucide-circle-minus: tests not implemented (either provider or consumer side)
    - :lucide-circle-dashed: no tests executed
                """.trimIndent()
        }

        val matrices = StringBuilder()

        for (binding in bindings) {
            val markdownCells = mutableListOf<MutableList<String>>()
            markdownCells.add(
                interopMatrix.providers
                    .filter { binding.json in it.bindings }
                    .map { libNames[it.id]!! + createTooltip(it) }
                    .toMutableList()
                    .apply {
                        add(0, "**Provider →**<br>**↓ Consumer**")
                    }
            )

//            val consumerLibs = libFeaturesFor(sortedLibs, Role.CONSUMER, binding)
//            val providerLibs = libFeaturesFor(sortedLibs, Role.PROVIDER, binding)

            for (consumerLib in interopMatrix.consumers) {
                val row =
                    listOf("**${libNames[consumerLib.id]!!}**${createTooltip(consumerLib)}").toMutableList()
                        .also {
                            markdownCells.add(it)
                        }

                for (providerLib in interopMatrix.providers) {
                    val testResult = interopMatrix.cellFor(binding, consumerLib.id, providerLib.id)

                    when (testResult) {
                        null -> row.add("&nbsp;")
                        else -> row.add(markdownForTestResult(testResult))
                    }
                }
            }

            matrices.append(
                Markdown.generate {
                    """
                        ${heading("Interoperability Matrix (${binding.humanReadableName} binding)", 1)}

                        <a href="javascript:window.history.back()" class="md-button">:lucide-arrow-big-left: Back</a>
                        <a href="${
                        htmlFileNameInteropMatrix(
                            src,
                            binding
                        )
                    }" target="_blank" class="md-button" title="Print view in new window">:lucide-printer: Print Version</a>

                        ${tableHeader(markdownCells.first())}
                    """
                }
            )

            for (row in markdownCells.drop(1)) {
                matrices.appendLine(
                    Markdown.tableRow(row)
                )
            }

            matrices.appendLine()
        }
        return listOf(header, legend, matrices.toString()).joinToString("\n")
    }

    private fun markdownForTestResult(
        src: InteroperabilityMatrix.Cell,
    ): String {
        val passedList = sortAndConcatenate(src.passedList)
        val failedList = sortAndConcatenate(src.failedList)
        val missingResultList = sortAndConcatenate(src.missingList)
        val notImplementedList = sortAndConcatenate(src.noneList)

        return mutableListOf<String>().apply {
            if (src.failedList.isEmpty() && src.missingList.isEmpty() && src.passedList.isEmpty()) {
                add(""":lucide-circle-dashed:{ title="No tests executed" }""")
            }

            if (src.failedList.isNotEmpty()) {
                if (src.verdict == Verdict.FAIL) {
                    add(""":lucide-circle-x:{ title="All implemented tests failed: $failedList" }""")
                } else {
                    add(""":lucide-circle-check:{ title="Tests passed: $passedList" }""")
                    add(""":lucide-circle-x:{ title="Failed tests: $failedList" }""")
                }
            } else {
                if (src.verdict == Verdict.PASS) {
                    if (src.passedList.isNotEmpty()) {
                        add(""":lucide-circle-check:{ title="All implemented tests passed: $passedList" }""")
                    }
                } else {
                    if (src.passedList.isNotEmpty()) {
                        add(""":lucide-circle-check:{ title="Tests passed: $passedList" }""")
                    }
                }
            }

            if (src.missingList.isNotEmpty()) {
                add(""":lucide-circle-question-mark:{ title="Missing test results: $missingResultList" }""")
            }

            if (src.noneList.isNotEmpty()) {
                add(""":lucide-circle-minus:{ title="Not implemented: $notImplementedList" }""")
            }
        }.joinToString(" ")
    }

    private fun createTooltip(lib: SdcLibraryFeatures): String {
        val features = sortAndConcatenate(lib.features.map { it.testCaseId })
        return """ { title="Version: ${lib.version.ifEmpty { "unknown" }}<br/>Features: $features" }"""
    }
}
