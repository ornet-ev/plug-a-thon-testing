package org.ornet.markdown

import org.ornet.TestCase
import org.ornet.TestGroup
import org.ornet.TestSequence
import org.ornet.sanitizedPath
import org.ornet.validTestCases

object TestSequenceMarkdownExport {
    fun indexMd(src: TestSequence): String {
        val list = src.testGroups.joinToString("") {
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
                ${frontMatter("icon" to "lucide/list")}
                # Test Sequence
                
                $list
            """
        }
    }

    fun testGroupMd(src: TestGroup, offsetLevel: Int = 0): String {
        val testCases = src.testCases.joinToString("\n") { testCaseMd(it, offsetLevel + 1) }
        return Markdown.generate {
            """
                ${frontMatter("icon" to "lucide/scroll-text")}
                ${Markdown.heading(src.name, offsetLevel)}
    
                $testCases
            """
        }
    }

    fun testCaseMd(src: TestCase, offsetLevel: Int): String {
        val description = when (src.deprecated) {
            true -> "(deprecated)"
            false -> src.description
        }

        return Markdown.generate {
            """
                ${Markdown.heading("Test case: ${src.id}", offsetLevel)}
                
                ${Markdown.heading("Preconditions", offsetLevel + 1)}
                
                ${src.preconditions.ifEmpty { "None" }}
                
                ${Markdown.heading("Description", offsetLevel + 1)}
                
                $description
            """
        }
    }
}