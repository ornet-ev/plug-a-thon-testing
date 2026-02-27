package org.ornet

import kotlinx.serialization.Serializable

// Stack modeling

@Serializable
data class SdcLibraries(
    val libraries: List<SdcLibrary>,
)

@Serializable
data class SdcLibrary(
    val id: String,
    val name: String,
    val roles: List<String> = listOf(), // consumer, provider
    val bindings: List<String> = listOf(), // dpws, protosdc
    val programmingLanguage: String = "",
    val license: String = "",
    val features: List<Feature>,
    val provider: String = "",
    val website: String = "",
    val contact: List<String> = emptyList(),
    val deprecated: Boolean = false,
)

@Serializable
data class SdcLibraryFeatures(
    val id: String,
    val version: String,
    val roles: List<String> = listOf(), // consumer, provider
    val bindings: List<String> = listOf(), // dpws, protosdc
    val features: List<Feature>,
)

@Serializable
data class Feature(
    val testCaseId: String,
    val bindings: List<String>? = null, // dpws, protosdc
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
    val binding: String,
    val caseIds: List<String>,
    val verdict: String, // pass, fail, none
)

const val ROLE_CONSUMER = "consumer"
const val ROLE_PROVIDER = "provider"
const val BINDING_DPWS = "dpws"
const val BINDING_PROTOSDC = "protosdc"

enum class Role(val json: String) {
    CONSUMER(ROLE_CONSUMER),
    PROVIDER(ROLE_PROVIDER);

    fun isIn(list: List<String>): Boolean {
        return json in list
    }

    companion object {
        fun fromJson(value: String) = entries.first { it.json == value }
    }
}

enum class Binding(val json: String) {
    DPWS(BINDING_DPWS),
    PROTOSDC(BINDING_PROTOSDC);

    fun isIn(list: List<String>): Boolean {
        return json in list
    }

    companion object {
        fun fromJson(value: String) = Binding.entries.first { it.json == value }
    }
}

data class InteroperabilityMatrix(
    val cells: List<Cell>,
) {

    fun cellFor(binding: Binding, consumerId: String, providerId: String): Cell? {
        return cells.firstOrNull {
            it.binding == binding
                    && it.consumerLibraryId == consumerId
                    && it.providerLibraryId == providerId
        }
    }

    data class Cell(
        val consumerLibraryId: String,
        val providerLibraryId: String,
        val binding: Binding,
        val verdict: Verdict,
        val failedList: List<String>,
        val missingList: List<String>,
    )
}


enum class Verdict(val json: String) {
    PASS("pass"),
    FAIL("fail"),
    PARTIAL("partial"),
    NONE("none"),
    ;

    companion object {
        fun fromJson(value: String) = Verdict.entries.first { it.json == value }
    }
}