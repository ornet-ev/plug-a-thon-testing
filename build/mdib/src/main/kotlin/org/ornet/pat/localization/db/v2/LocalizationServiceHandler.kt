package org.ornet.pat.localization.db.v2

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlIndentation
import com.akuleshov7.ktoml.TomlOutputConfig
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.somda.dsl.biceps.Mdib
import org.somda.dsl.biceps.MdibPostProcessor
import org.somda.dsl.biceps.base.tree.ComponentTree
import org.somda.dsl.biceps.base.tree.Descriptor
import java.io.File

class LocalizationServiceHandler(private val directory: File) : MdibPostProcessor {
    override val name: String = "LocalizationServiceHandler"

    private var refCount = 0

    private val toml = Toml(
        outputConfig = TomlOutputConfig(
            // indentation symbols for serialization, default 4 spaces
            indentation = TomlIndentation.NONE,
        )
    )

    private val json = Json {
        prettyPrint = true
    }

    override fun run(
        mdib: Mdib,
        componentTree: ComponentTree.Root,
    ) {
        // localized text collector
        val localizedTexts = mutableListOf<LocalizedText>()

        // traverse mdib and add concept descriptions with refs
        componentTree.traversePreOrder { node ->
            when (val c = node.component) {
                is Descriptor<*> -> if (c.type?.conceptDescriptions?.isNotEmpty() == true) {
                    val lt = c.type!!.conceptDescriptions.first()
                    lt.lang?.also { lang ->
                        if (lt.textWidth == null) {
                            lt.textWidth(textWidthForText(lang, lt.value))
                        }
                    }

                    val nextRef = nextTextReference()

                    // add text from mdib to localization list
                    LocalizedText(
                        language = lt.lang ?: "",
                        value = lt.value,
                        ref = nextRef,
                        width = lt.textWidth?.serialize() ?: "",
                        lines = lineCount(lt.value),
                        version = lt.version?.toLong() ?: 0L
                    ).also { localizedText ->
                        localizedTexts.add(localizedText)

                        // latin "translation"
                        localizedTexts.add(
                            createTranslation(
                                localizedText,
                                LoremIpsum.CharacterSet.LATIN,
                                "de"
                            )
                        )

                        // greek
                        localizedTexts.add(
                            createTranslation(
                                localizedText,
                                LoremIpsum.CharacterSet.GREEK,
                                "el-GR"
                            )
                        )

                        // chinese
                        localizedTexts.add(
                            createTranslation(
                                localizedText,
                                LoremIpsum.CharacterSet.CHINESE,
                                "zh-CN"
                            )
                        )
                    }

                    // add reference to resulting mdib
                    c.type?.conceptDescriptionRef(nextRef, 1.toBigInteger())
                }
            }
        }

        // add another version of texts
        localizedTexts.map {
            it.copy(version = 1L)
        }.also {
            localizedTexts.addAll(it)
        }

        // write localized texts to toml
        toml.encodeToString(LocalizedTextModel(localizedTexts)).also {
            directory.mkdirs()
            File(
                directory,
                mdib.name + FILE_NAME_SUFFIX + FILE_EXTENSION_SUFFIX_TOML
            ).writeText(it)
        }

        json.encodeToString(localizedTexts).also {
            directory.mkdirs()
            File(
                directory,
                mdib.name + FILE_NAME_SUFFIX + FILE_EXTENSION_SUFFIX_JSON
            ).writeText(it)
        }
    }

    private fun createTranslation(
        base: LocalizedText,
        characterSet: LoremIpsum.CharacterSet,
        lang: String,
    ): LocalizedText {
        val amount = base.value.split(" ").count()
        val text = LoremIpsum(characterSet).getWords(amount)
        return base.copy(
            language = lang,
            value = text,
            width = textWidthForText(lang, text).serialize(),
            lines = lineCount(text),
        )
    }

    private fun nextTextReference(): String = "text_ref_${refCount++}"

    private companion object {
        const val FILE_NAME_SUFFIX = "LocalizedTextsV2"
        const val FILE_EXTENSION_SUFFIX_TOML = ".toml"
        const val FILE_EXTENSION_SUFFIX_JSON = ".json"
    }
}