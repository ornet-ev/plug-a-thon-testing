package org.ornet.pat.localization.db.v1

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlIndentation
import com.akuleshov7.ktoml.TomlOutputConfig
import de.svenjacobs.loremipsum.LoremIpsum
import kotlinx.serialization.encodeToString
import org.somda.dsl.biceps.Mdib
import org.somda.dsl.biceps.MdibPostProcessor
import org.somda.dsl.biceps.base.LocalizedTextWidth
import org.somda.dsl.biceps.base.tree.ComponentTree
import org.somda.dsl.biceps.base.tree.Descriptor
import java.io.File

class LocalizationServiceHandler(private val directory: File) : MdibPostProcessor {
    override val name: String = "LocalizationServiceHandler"

    private var refCount = 0

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
                    val nextRef = nextTextReference()

                    // add text from mdib to localization list
                    LocalizedText(
                        language = lt.lang ?: "",
                        value = lt.value ?: "",
                        ref = nextRef,
                        width = lt.textWidth?.name?.lowercase() ?: LocalizedTextWidth.M.name.lowercase(),
                        lines = lt.value?.split("\n")?.size ?: 1,
                        version = lt.version?.toLong() ?: 0L
                    ).also { localizedText ->
                        // add a "german" translation
                        localizedTexts.add(localizedText)
                        localizedTexts.add(
                            localizedText.copy(
                                language = "de",
                                value = LoremIpsum().getWords(localizedText.value.split(" ").count()),
                            )
                        )
                    }

                    // add reference to resulting mdib
                    c.type?.conceptDescriptionRef(nextRef)
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
        Toml(
            outputConfig = TomlOutputConfig(
                // indentation symbols for serialization, default 4 spaces
                indentation = TomlIndentation.NONE,
            )
        ).encodeToString(TomlModel(localizedTexts)).also {
            directory.mkdirs()
            File(
                directory,
                mdib.name + FILE_NAME_SUFFIX + FILE_EXTENSION_SUFFIX
            ).writeText(it)
        }
    }

    private fun nextTextReference(): String = "text_ref_${refCount++}"

    private companion object {
        const val FILE_NAME_SUFFIX = "LocalizedTextsV1"
        const val FILE_EXTENSION_SUFFIX = ".toml"
    }
}