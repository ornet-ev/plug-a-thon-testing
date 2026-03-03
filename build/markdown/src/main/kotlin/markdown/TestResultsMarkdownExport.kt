package org.ornet.markdown

import org.ornet.Binding
import org.ornet.InteroperabilityMatrix
import org.ornet.PatEvent
import org.ornet.Role
import org.ornet.SdcLibrary
import org.ornet.SdcLibraryFeatures
import org.ornet.TestSequence
import org.ornet.Verdict
import org.ornet.createInteroperabilityMatrix
import org.ornet.htmlFileNameInteropMatrix
import org.ornet.libFeaturesFor


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
            libFeatures
        )

        val libsForPat = libFeatures.associateBy { it.id }
        val versionMarkdown = libFeatures.associate { lib ->
            lib.id to lib.version.let {
                """ { title="Version: ${it.ifEmpty { "unknown" }}" }"""
            }
        }

        val libNames = libraries.associate { it.id to it.name }
        val sortedLibs = libraries.sortedBy { it.name }.map { libsForPat[it.id]!! }

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
    - :lucide-check: all featured tests succeeded
    - :lucide-x: all featured tests failed
    - :lucide-triangle-alert: some tests failed
    - :lucide-circle-question-mark: missing test results
    - empty: no tests executed
                """.trimIndent()
        }

        val matrices = StringBuilder()

        for (binding in bindings) {
            val markdownCells = mutableListOf<MutableList<String>>()
            markdownCells.add(
                sortedLibs
                    .map { it }
                    .filter { Role.PROVIDER.json in it.roles }
                    .filter { binding.json in it.bindings }
                    .map { libNames[it.id]!! + versionMarkdown[it.id]!! }
                    .toMutableList()
                    .apply {
                        add(0, "**Provider →**<br>**↓ Consumer**")
                    }
            )

            val consumerLibs = libFeaturesFor(sortedLibs, Role.CONSUMER, binding)
            val providerLibs = libFeaturesFor(sortedLibs, Role.PROVIDER, binding)

            for (consumerLib in consumerLibs) {
                val row = listOf("**${libNames[consumerLib.id]!!}**${versionMarkdown[consumerLib.id]!!}").toMutableList().also {
                    markdownCells.add(it)
                }

                for (providerLib in providerLibs) {
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
                        <a href="${htmlFileNameInteropMatrix(src, binding)}" target="_blank" class="md-button" title="Print view in new window">:lucide-printer: Print Version</a>

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
        val failedList = src.failedList.sorted().joinToString(", ")
        val missingResultList = src.missingList.sorted().joinToString(", ")

        return mutableListOf<String>().apply {
            if (src.failedList.isNotEmpty()) {
                if (src.verdict == Verdict.FAIL) {
                    add(""":lucide-x:{ title="All tests failed" }""")
                } else {
                    add(""":lucide-triangle-alert:{ title="Failed tests: $failedList" }""")
                }
            } else {
                if (src.verdict == Verdict.PASS) {
                    add(""":lucide-check:{ title="All tests passed" }""")
                }
            }

            if (src.missingList.isNotEmpty()) {
                add(""":lucide-circle-question-mark:{ title="Missing test results: $missingResultList" }""")
            }
        }.joinToString(" ")
    }
}
