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

    val failedIds = testResults
        .filter { Verdict.fromJson(it.verdict) == Verdict.FAIL }
        .map { it.caseIds }
        .flatten()
        .subtract(deprecatedIds)

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
        if (allFeaturedIds != missingResultIds) missingResultIds else listOf()
    )
}

//
//fun testResultsFor(
//    src: List<TestResult>,
//    testSequence: TestSequence,
//    consumerLibrary: SdcLibraryFeatures,
//    providerLibrary: SdcLibraryFeatures,
//): Pair<MarkdownText, HtmlTestResult> {
//    // find all tests that are not deprecated, and where there is potential
//    // support by consumer and provider side
//    val allFeaturedIds = testSequence
//        .validTestCases()
//        .map { it.id }
//        .filter {
//            supportsTestCaseFromConsumerSide(it, consumerLibrary) &&
//                    supportsTestCaseFromProviderSide(it, providerLibrary)
//        }
//
//    val deprecatedIds = testSequence.deprecatedTestCases().map { it.id }
//
//    val passedIds =
//        src.filter { Verdict.fromJson(it.verdict) == Verdict.PASS }.map { it.caseIds }.flatten().subtract(deprecatedIds)
//    val failedIds = src.filter { it.verdict == "fail" }.map { it.caseIds }.flatten().subtract(deprecatedIds)
//    val missingResult = (allFeaturedIds - passedIds - failedIds)
//
//    val failedList = failedIds.sorted().joinToString(", ")
//    val missingResultList = missingResult.sorted().joinToString(", ")
//    val htmlResult = mutableListOf<String>()
//    var verdict = "none"
//
//    return mutableListOf<String>().apply {
//        if (failedList.isNotEmpty()) {
//            if ((allFeaturedIds - failedIds).isEmpty()) {
//                add(":lucide-x:")
//                htmlResult.add("""❌""")
//                verdict = "fail"
//            } else {
//                add(":lucide-triangle-alert: $failedList")
//                htmlResult.add("""⚠ $failedList""")
//                verdict = "partial"
//            }
//        } else {
//            if (missingResult.isEmpty()) {
//                add(":lucide-check:")
//                htmlResult.add("""✓""")
//                verdict = "pass"
//            }
//        }
//
//        if (missingResult.isNotEmpty()) {
//            if (allFeaturedIds != missingResult) {
//                add(":lucide-circle-question-mark: $missingResultList")
//                htmlResult.add("""❓$missingResultList""")
//                verdict = "partial"
//            }
//        }
//    }.joinToString("<br>") to HtmlTestResult(verdict, htmlResult.joinToString("<br/>"))
//}