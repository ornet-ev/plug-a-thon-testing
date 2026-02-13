package org.ornet.markdown

import org.ornet.SdcLibrary
import org.ornet.TestSequence
import org.ornet.sanitizedPath
import org.ornet.testGroupForTestCase

object SdcLibrariesMarkdownExport {
    fun indexMd(src: List<SdcLibrary>): String {
        val list = src.filterNot { it.deprecated }.joinToString("") {
            Markdown.generate {
                listItem(
                    link(
                        sanitizedPath(it.id),
                        it.name
                    )
                )
            }
        }

        return Markdown.generate {
            """
                ${frontMatter("icon" to "lucide/boxes", "title" to "'SDC Libraries'")}
                ${heading("SDC Libraries")}
                
                $list
            """
        }
    }

    fun sdcLibraryMd(src: SdcLibrary, testSequence: TestSequence): String {
        val properties = Markdown.generate {
            val builder = StringBuilder()
            builder.appendLine(tableRow("Library ID:", "`${src.id}`"))
            builder.appendLine(tableRow("Name:", src.name))
            if (src.manufacturer.isNotEmpty()) builder.appendLine(tableRow("Manufacturer:", src.manufacturer))
            if (src.website.isNotEmpty()) builder.appendLine(tableRow("Website:", src.website))
            if (src.contact.isNotEmpty()) builder.appendLine(
                tableRow(
                    "Contact:", multilineCell(
                        src.contact.joinToString("\n")
                    )
                )
            )
            builder.appendLine(
                tableRow(
                    "Roles:",
                    src.roles.ifEmpty { listOf("None") }.map { roleNameFor(it) }.let { multilineCell(it) })
            )
            if (src.programmingLanguage.isEmpty()) builder.appendLine(
                tableRow(
                    "Programming Language:",
                    src.programmingLanguage
                )
            )
            src.available.ifEmpty { "None" }.also { builder.appendLine(tableRow("Availability", it)) }
            builder.toString()
        }

        val features = src.features.mapNotNull { feature ->
            testSequence.testGroupForTestCase(feature.testCaseId)?.let {
                Markdown.generate {
                    listItem(
                        link(
                            "../test-sequence/${sanitizedPath(it.id)}/#test-case-${feature.testCaseId}",
                            "Test case ${feature.testCaseId}"
                        )
                    )
                }
            }
        }.joinToString("\n").let {
            it.ifEmpty { "Currently no information available." }
        }

        return Markdown.generate {
            """
                ${frontMatter("icon" to "lucide/box")}
                ${heading(src.name)}

                ${heading("General library information", 1)}

                ${Markdown.tableHeader(listOf("Property", "Description"))}
                $properties
                
                ## Implemented features

                $features
            """
        }
    }
}