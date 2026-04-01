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
    sdcLibrary: SdcLibraryFeatures,
    testCaseId: String,
    binding: Binding,
): Boolean {
    return supportsTestCase(sdcLibrary, testCaseId, Role.CONSUMER, binding)
}

// true if a test case is supported by a provider, false otherwise
fun supportsTestCaseFromProviderSide(
    sdcLibrary: SdcLibraryFeatures,
    testCaseId: String,
    binding: Binding,
): Boolean {
    return supportsTestCase(sdcLibrary, testCaseId, Role.PROVIDER, binding)
}

private fun supportsTestCase(
    sdcLibrary: SdcLibraryFeatures,
    testCaseId: String,
    role: Role,
    binding: Binding,
): Boolean {
    val potentialMatch = sdcLibrary.features.firstOrNull {
        it.testCaseId == testCaseId && it.supported
    } ?: return false

    val effectiveRoles = potentialMatch.roles ?: sdcLibrary.roles
    val effectiveBindings = potentialMatch.bindings ?: sdcLibrary.bindings
    return role.json in effectiveRoles && binding.json in effectiveBindings
}

fun htmlFileNameInteropMatrix(patEvent: PatEvent, binding: Binding): String {
    return "pat-${patEvent.patNumber}-${binding.json}.html"
}

fun libFeaturesFor(libFeatures: List<SdcLibraryFeatures>, role: Role, binding: Binding): List<SdcLibraryFeatures> {
    return libFeatures.filter { role.json in it.roles }.filter { binding.json in it.bindings }
}