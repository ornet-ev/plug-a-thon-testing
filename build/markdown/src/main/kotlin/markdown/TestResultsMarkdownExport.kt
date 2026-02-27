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
        val interopMatrix = createInteroperabilityMatrix(
            src,
            testSequence,
            libFeatures
        )

        val libsForPat = libFeatures.map { it.id }
        val versionMarkdown = libFeatures.associate { lib ->
            lib.id to lib.version.let {
                """ { title="Version: ${it.ifEmpty { "unknown" }}" }"""
            }
        }
        val sortedLibs = libraries.sortedBy { it.name }.filter { it.id in libsForPat }

        val markdownCells = mutableListOf<MutableList<String>>()
        markdownCells.add(
            sortedLibs
                .map { it }
                .filter { Role.PROVIDER.json in it.roles }
                .map { it.name + versionMarkdown[it.id]!! }
                .toMutableList()
                .apply {
                    add(0, "**Provider →**<br>**↓ Consumer**")
                }
        )

        val consumerLibs = sortedLibs.filter { Role.CONSUMER.json in it.roles }
        val providerLibs = sortedLibs.filter { Role.PROVIDER.json in it.roles }

        for (consumerLib in consumerLibs) {
            val row = listOf("**${consumerLib.name}**${versionMarkdown[consumerLib.id]!!}").toMutableList().also {
                markdownCells.add(it)
            }

            for (providerLib in providerLibs) {
                val testResult = interopMatrix.cellFor(Binding.DPWS, consumerLib.id, providerLib.id)

                when (testResult) {
                    null -> row.add("&nbsp;")
                    else -> row.add(markdownForTestResult(testResult))
                }
            }
        }

        return Markdown.generate {
            val icon = "icon" to "lucide/circle-check-big"
            val hideSidebars = "hide" to frontMatterListValues("navigation", "toc")
            val builder = StringBuilder()
            builder.append(
                """
                    ${frontMatter(icon, hideSidebars)}

                    ${heading("PAT#${src.patNumber}")}
                    
                    ${heading("Interoperability Matrix", 1)}

                    <a href="javascript:window.history.back()" class="md-button">:lucide-arrow-big-left: Back</a>
                    <a href="print.html" target="_blank" class="md-button" title="Print view in new window">:lucide-printer: Print Version</a>

                    ${tableHeader(markdownCells.first())}
                """
            )

            for (row in markdownCells.drop(1)) {
                builder.appendLine(
                    Markdown.tableRow(row)
                )
            }

            builder.toString()
        } + Markdown.generate(false) {
            """
??? Legend
    - :lucide-check: all featured tests succeeded
    - :lucide-x: all featured tests failed
    - :lucide-triangle-alert: some tests failed
    - :lucide-circle-question-mark: missing test results
    - empty: no tests executed
                """.trimIndent()
        }
    }

    private fun markdownForTestResult(
        src: InteroperabilityMatrix.Cell,
    ): String {
        val failedList = src.failedList.sorted().joinToString(", ")
        val missingResultList = src.missingList.sorted().joinToString(", ")

        return mutableListOf<String>().apply {
            if (src.failedList.isNotEmpty()) {
                if (src.verdict == Verdict.FAIL) {
                    add(":lucide-x:")
                } else {
                    add(":lucide-triangle-alert: $failedList")
                }
            } else {
                if (src.verdict == Verdict.PASS) {
                    add(":lucide-check:")
                }
            }

            if (src.missingList.isNotEmpty()) {
                add(":lucide-circle-question-mark: $missingResultList")
            }
        }.joinToString("<br>")
    }
}