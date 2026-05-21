package org.ornet

fun createInteroperabilityMatrix(
    src: PatEvent,
    testSequence: TestSequence,
    libFeatures: List<SdcLibraryFeatures>,
): InteroperabilityMatrix {
    val interopMatrixCells = mutableListOf<InteroperabilityMatrix.Cell>()

    val consumerLibs = libFeatures.filter { Role.CONSUMER.json in it.roles }
    val providerLibs = libFeatures.filter { Role.PROVIDER.json in it.roles }

    for (binding in Binding.entries) {
        for (consumerLib in consumerLibs) {
            for (providerLib in providerLibs) {
                val testResults = src.resultsFor(consumerLib.id, providerLib.id)
                interopMatrixCells.add(
                    createInteroperabilityMatrixCell(
                        testResults,
                        testSequence,
                        consumerLib,
                        providerLib,
                        binding
                    )
                )
            }
        }
    }

    return InteroperabilityMatrix(interopMatrixCells)
}

fun createInteroperabilityMatrixCell(
    src: List<TestResult>,
    testSequence: TestSequence,
    consumerLibrary: SdcLibraryFeatures,
    providerLibrary: SdcLibraryFeatures,
    binding: Binding,
): InteroperabilityMatrix.Cell {
    val testResults = src.filter { Binding.fromJson(it.binding) == binding }

    // find all tests that are not deprecated, and where there is potential
    // support by consumer and provider side
    val allFeaturedIds = testSequence
        .validTestCases()
        .map { it.id }
        .filter {
            supportsTestCaseFromConsumerSide(consumerLibrary, it, binding) &&
                    supportsTestCaseFromProviderSide(providerLibrary, it, binding)
        }

    val deprecatedIds = testSequence.deprecatedTestCases().map { it.id }

    val passedIds = testResults
        .filter { Verdict.fromJson(it.verdict) == Verdict.PASS }
        .map { it.caseIds }.flatten().subtract(deprecatedIds)

    val notImplementedIds = testSequence.validTestCases().map { it.id } - allFeaturedIds

    val failedIds = testResults
        .filter { Verdict.fromJson(it.verdict) == Verdict.FAIL }
        .map { it.caseIds }
        .flatten()
        .subtract(deprecatedIds)
        .subtract(notImplementedIds)

    val missingResultIds = (allFeaturedIds - passedIds - failedIds)

    var verdict = Verdict.NONE
    if (failedIds.isNotEmpty()) {
        verdict = if ((allFeaturedIds - failedIds).isEmpty()) {
            Verdict.FAIL
        } else {
            Verdict.PARTIAL
        }
    } else {
        if (missingResultIds.isEmpty()) {
            verdict = Verdict.PASS
        }
    }

    if (missingResultIds.isNotEmpty()) {
        if (allFeaturedIds != missingResultIds) {
            verdict = Verdict.PARTIAL
        }
    }

    return InteroperabilityMatrix.Cell(
        consumerLibrary.id,
        providerLibrary.id,
        binding,
        verdict,
        failedIds.toList(),
        if (allFeaturedIds != missingResultIds) missingResultIds else listOf(),
        passedIds.toList(),
        notImplementedIds
    )
}