package org.ornet.pat.localization.db.v1

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TomlModel(
    @SerialName("localizedText")
    val localizedTexts: List<LocalizedText>,
)

@Serializable
data class LocalizedText(
    val language: String,
    val value: String,
    val ref: String,
    val width: String,
    val lines: Int,
    val version: Long,
)

