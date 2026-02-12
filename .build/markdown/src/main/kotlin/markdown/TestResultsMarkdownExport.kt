package org.ornet.markdown

import org.ornet.PatEvent
import org.ornet.ROLE_CONSUMER
import org.ornet.ROLE_PROVIDER
import org.ornet.SdcLibrary
import org.ornet.TestResult
import org.ornet.TestSequence
import org.ornet.deprecatedTestCases
import org.ornet.resultsFor
import org.ornet.supportsTestCaseFromConsumerSide
import org.ornet.supportsTestCaseFromProviderSide
import org.ornet.validTestCases

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
    ): String {
        val sortedLibs = libraries.sortedBy { it.name }
        val interopMatrix = mutableListOf<MutableList<String>>()
        interopMatrix.add(
            sortedLibs
                .map { it }
                .filter { ROLE_PROVIDER in it.roles }
                .filterNot { it.deprecated }
                .map { it.name }.toMutableList().apply {
                    add(0, "**Provider →**<br>**↓ Consumer**")
                }
        )

        val consumerLibs = sortedLibs.filter { ROLE_CONSUMER in it.roles }.filterNot { it.deprecated }
        val providerLibs = sortedLibs.filter { ROLE_PROVIDER in it.roles }.filterNot { it.deprecated }

        for (consumerLib in consumerLibs) {
            val row = listOf("**${consumerLib.name}**").toMutableList().also {
                interopMatrix.add(it)
            }

            for (providerLib in providerLibs) {
                val testResults = src.resultsFor(consumerLib.id, providerLib.id)
                when (testResults.isEmpty()) {
                    true -> row.add("&nbsp;")
                    false -> row.add(testResultsFor(testResults, testSequence, consumerLib, providerLib))
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
                    <a href="javascript:window.alert('Not implemented yet')" class="md-button" title="Not implemented yet">:lucide-printer: Print Version</a>
                    
                    ${tableHeader(interopMatrix.first())}
                """
            )

            for (row in interopMatrix.drop(1)) {
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

    private fun testResultsFor(
        src: List<TestResult>,
        testSequence: TestSequence,
        consumerLibrary: SdcLibrary,
        providerLibrary: SdcLibrary,
    ): String {
        // find all tests that are not deprecated, and where there is potential
        // support by consumer and provider side
        val allFeaturedIds = testSequence
            .validTestCases()
            .map { it.id }
            .filter {
                supportsTestCaseFromConsumerSide(it, consumerLibrary) &&
                        supportsTestCaseFromProviderSide(it, providerLibrary)
            }

        val deprecatedIds = testSequence.deprecatedTestCases().map { it.id }

        val passedIds = src.filter { it.verdict == "pass" }.map { it.caseIds }.flatten().subtract(deprecatedIds)
        val failedIds = src.filter { it.verdict == "fail" }.map { it.caseIds }.flatten().subtract(deprecatedIds)
        val missingResult = (allFeaturedIds - passedIds - failedIds)

        val failedList = failedIds.sorted().joinToString(", ")
        val missingResultList = missingResult.sorted().joinToString(", ")
        return mutableListOf<String>().apply {
            if (failedList.isNotEmpty()) {
                if ((allFeaturedIds - failedIds).isEmpty()) {
                    add(":lucide-x:")
                } else {
                    add(":lucide-triangle-alert: $failedList")
                }
            } else {
                if (missingResult.isEmpty()) {
                    add(":lucide-check:")
                }
            }

            if (missingResult.isNotEmpty()) {
                if (allFeaturedIds != missingResult) {
                    add(":lucide-circle-question-mark: $missingResultList")
                }
            }
        }.joinToString("<br>")
    }
}