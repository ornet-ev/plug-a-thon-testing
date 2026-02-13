package org.ornet.markdown

import org.ornet.ROLE_CONSUMER
import org.ornet.ROLE_PROVIDER

// markdown generation utils
object Markdown {
    fun heading(title: String, level: Int = 0): String {
        return StringBuilder().apply {
            repeat(level + 1) { append("#") }
            append(" ")
            append(title)
        }.toString()
    }

    fun tableHeader(row: List<String>): String {
        return StringBuilder().apply {

            append(
                row.joinToString(
                    separator = " | ",
                    prefix = "| ",
                    postfix = " |\n",
                ) {
                    it
                }
            )

            append(
                row.joinToString(
                    separator = " | ",
                    prefix = "| ",
                    postfix = " |",
                ) {
                    StringBuilder().apply {
                        repeat(it.length) { append("-") }
                    }.toString()
                }
            )
        }.toString()
    }

    fun tableRow(row: List<String>): String {
        return row.joinToString(
            separator = " | ",
            prefix = "| ",
            postfix = " |",
        ) {
            it
        }
    }

    fun tableRow(vararg cell: String): String {
        return tableRow(arrayOf(*cell).toList())
    }

    fun multilineCell(content: String): String {
        return content.replace("\n", "<br>")
    }

    fun multilineCell(content: List<String>): String {
        return content.joinToString("<br>")
    }

    fun link(url: String, title: String = url): String {
        return "[$title]($url)"
    }

    fun listItem(text: String): String {
        return "- ${text.replace("\n", "<br>")}"
    }

    fun generate(trimIndent: Boolean = true, action: Markdown.() -> String): String {
        if (!trimIndent) return this.action()
        return this.action()
            .trim()
            .split("\n").joinToString("") { "${it.trim()}\n" }
    }

    fun frontMatterListValues(values: List<String>): String {
        return values.joinToString(
            separator = "\n",
            prefix = "\n"
        ) { "  - ${it.trim()}" }
    }

    fun frontMatterListValues(vararg value: String): String {
        return frontMatterListValues(arrayOf(*value).toList())
    }

    fun frontMatter(items: Map<String, String>): String {
        return generate {
            """
                ---
                ${items.map { (key, value) -> "$key: $value" }.joinToString("\n")}
                ---
            """
        }
    }

    fun frontMatter(vararg item: Pair<String, String>): String {
        return frontMatter(arrayOf(*item).associate { it.first to it.second })
    }
}

// mapping of consumer/provider role to human-readable, official terms
fun roleNameFor(name: String): String {
    return when (name) {
        ROLE_CONSUMER -> "SOMDS Consumer"
        ROLE_PROVIDER -> "SOMDS Provider"
        else -> error("Unknown role: $name")
    }
}