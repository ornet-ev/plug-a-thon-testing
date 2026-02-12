package org.ornet

import kotlinx.serialization.Serializable

// Stack modeling

@Serializable
data class SdcLibrary(
    val id: String,
    val name: String,
    val roles: List<String> = listOf(), // consumer, provider
    val programmingLanguage: String = "",
    val available: String = "",
    val features: List<Feature>,
    val manufacturer: String = "",
    val website: String = "",
    val contact: List<String> = emptyList(),
    val deprecated: Boolean = false,
)

@Serializable
data class Feature(
    val testCaseId: String,
    val roles: List<String>? = null, // consumer, provider
)

// Test modeling

@Serializable
data class TestSequence(
    val testGroups: List<TestGroup>,
)

@Serializable
data class TestGroup(
    val id: String,
    val name: String,
    val testCases: List<TestCase>,
)

@Serializable
data class TestCase(
    val id: String,
    val preconditions: String, // markdown
    val description: String, // markdown
    val deprecated: Boolean = false,
)

// Test result modeling

@Serializable
data class PatEvent(
    val patNumber: Int,
    val startDate: String,
    val endDate: String,
    val website: String,
    val testResults: List<TestResult> = listOf(),
)

@Serializable
data class TestResults(
    val patNumber: Int,
    val testResults: List<TestResult>,
)

@Serializable
data class TestResult(
    val consumerLibraryId: String,
    val providerLibraryId: String,
    val caseIds: List<String>,
    val verdict: String, // pass, fail, none
)

const val ROLE_CONSUMER = "consumer"
const val ROLE_PROVIDER = "provider"