package org.ornet

// all test cases without deprecated ones
fun TestSequence.validTestCases(): List<TestCase> {
    return testGroups.map { it.testCases }.flatten().filterNot { it.deprecated }
}

// only deprecated test cases
fun TestSequence.deprecatedTestCases(): List<TestCase> {
    return testGroups.map { it.testCases }.flatten().filter { it.deprecated }
}

// find enclosing group for test case
fun TestSequence.testGroupForTestCase(testCaseId: String): TestGroup? {
    return testGroups.firstOrNull {
        it.testCaseFor(testCaseId) != null
    }
}

// find test case in group
private fun TestGroup.testCaseFor(id: String): TestCase? {
    return testCases.firstOrNull {
        it.id == id
    }
}

// find test results for a certain consumer and provider library pair
fun PatEvent.resultsFor(consumerLibraryId: String, providerLibraryId: String): List<TestResult> {
    return testResults.filter {
        it.consumerLibraryId == consumerLibraryId && it.providerLibraryId == providerLibraryId
    }
}

fun sanitizedPath(src: String): String {
    return src
        .replace("_", "-")
        .replace(" ", "-")
        .lowercase()
}

// true if a test case is supported by a consumer, false otherwise
fun supportsTestCaseFromConsumerSide(
    testCaseId: String,
    sdcLibrary: SdcLibrary,
): Boolean {
    return supportsTestCase(testCaseId, sdcLibrary, ROLE_CONSUMER)
}

// true if a test case is supported by a provider, false otherwise
fun supportsTestCaseFromProviderSide(
    testCaseId: String,
    sdcLibrary: SdcLibrary,
): Boolean {
    return supportsTestCase(testCaseId, sdcLibrary, ROLE_PROVIDER)
}

private fun supportsTestCase(
    testCaseId: String,
    sdcLibrary: SdcLibrary,
    role: String,
): Boolean {
    val potentialMatch = sdcLibrary.features.firstOrNull {
        it.testCaseId == testCaseId
    } ?: return false

    val effectiveRoles = potentialMatch.roles ?: sdcLibrary.roles

    return role in effectiveRoles
}