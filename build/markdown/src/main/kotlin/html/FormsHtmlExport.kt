package org.ornet.html

import org.ornet.PatEvent
import org.ornet.Role
import org.ornet.SdcLibrary
import org.ornet.TestSequence

object FormsHtmlExport {
    private const val RESOURCE_NAME_MANIFEST_FORM = "/library-manifest-form.html"
    private const val RESOURCE_NAME_TEST_RESULTS_FORM = "/test-results-form.html"

    private val manifestFormHtml = FormsHtmlExport::class.java.getResource(RESOURCE_NAME_MANIFEST_FORM)?.readText()
        ?: error("Resource not found: $RESOURCE_NAME_MANIFEST_FORM")

    private val testResultsFormHtml =
        FormsHtmlExport::class.java.getResource(RESOURCE_NAME_TEST_RESULTS_FORM)?.readText()
            ?: error("Resource not found: $RESOURCE_NAME_TEST_RESULTS_FORM")

    fun libraryManifestFormHtml(
        testSequence: TestSequence,
    ): String {
        return createHtmlWithTestCases(
            testSequence,
            manifestFormHtml
        )
    }

    fun testResultsFormHtml(
        testSequence: TestSequence,
        patEvents: List<PatEvent>,
        sdcLibraries: List<SdcLibrary>,
    ): String {
        val consumerIds = sdcLibraries.filter {
            Role.CONSUMER.json in it.roles
        }.map { it.id }
        val providerIds = sdcLibraries.filter {
            Role.PROVIDER.json in it.roles
        }.map { it.id }

        return createHtmlWithTestCases(testSequence, testResultsFormHtml)
            .replace("\$PAT_NUMBER", patEvents.maxOf { it.patNumber + 1 }.toString())
            .replace("'\$CONSUMER_IDS'", consumerIds.joinToString(separator = "', '", prefix = "'", postfix = "'"))
            .replace("'\$PROVIDER_IDS'", providerIds.joinToString(separator = "', '", prefix = "'", postfix = "'"))
    }

    private fun createHtmlWithTestCases(
        testSequence: TestSequence,
        template: String,
    ): String {
        val testCases = testSequence.testGroups.flatMap { it.testCases }.filterNot { it.deprecated }.map { it.id }.sorted()
        return template.replace(
            "'\$TEST_CASE_IDS'",
            testCases.joinToString(separator = "', '", prefix = "'", postfix = "'")
        )
    }
}