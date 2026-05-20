package org.ornet.html

import org.ornet.Binding
import org.ornet.InteroperabilityMatrix
import org.ornet.PatEvent
import org.ornet.Role
import org.ornet.SdcLibrary
import org.ornet.SdcLibraryFeatures
import org.ornet.TestSequence
import org.ornet.Verdict
import org.ornet.createInteroperabilityMatrix
import org.ornet.libFeaturesFor

object TestResultsHtmlExport {
    fun patEventHtml(
        src: PatEvent,
        testSequence: TestSequence,
        libraries: List<SdcLibrary>,
        libFeatures: List<SdcLibraryFeatures>,
        binding: Binding,
    ): String {
        val interopMatrix = createInteroperabilityMatrix(
            src,
            testSequence,
            libFeatures
        )

        val libsForPat = libFeatures.associateBy { it.id }
        val versionHtml = libFeatures.associate { lib ->
            lib.id to lib.version.let {
                val zwAdded = addZeroWidthSpace(it)
                """<div class="version-badge">${zwAdded.ifEmpty { "n/a" }}</div>"""
            }
        }
        val libNames = libraries.associate { it.id to it.name }
        val sortedLibs = libraries.sortedBy { it.name }.mapNotNull { libsForPat[it.id] }

        val htmlCells = mutableListOf<MutableList<String>>()
        htmlCells.add(
            sortedLibs
                .map { it }
                .filter { Role.PROVIDER.json in it.roles }
                .filter { binding.json in it.bindings }
                .map {
                    """<th><div>${libNames[it.id]!!}</div>${versionHtml[it.id]!!}</th>"""
                }
                .toMutableList()
        )

        val consumerLibs = libFeaturesFor(sortedLibs, Role.CONSUMER, binding)
        val providerLibs = libFeaturesFor(sortedLibs, Role.PROVIDER, binding)

        for (consumerLib in consumerLibs) {
            val row =
                listOf("<td><div>${libNames[consumerLib.id]!!}</div>${versionHtml[consumerLib.id]!!}</td>").toMutableList()
                    .also {
                        htmlCells.add(it)
                    }

            for (providerLib in providerLibs) {
                val testResult = interopMatrix.cellFor(binding, consumerLib.id, providerLib.id)
                when (testResult) {
                    null -> row.add("""<td class="result-cell none">&nbsp;</td>""")
                    else -> row.add("""<td class="result-cell ${testResult.verdict.json}">${htmlForTestResult(testResult)}</td>""")
                }
            }
        }

        val title = "PAT#${src.patNumber} Interoperability Matrix (${binding.humanReadableName} binding)"

        return """
            <html lang="en">
            <head>
                <title>$title</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }

                    html, body {
                        width: 100%;
                        font-size: 1em;
                    }

                    body {
                        display: flex;
                        flex-direction: column;
                        font-family: Arial, Helvetica, sans-serif;
                        padding: 16px;
                        
                    }

                    h1 {
                        flex-shrink: 0;
                    }

                    table {
                        width: 100%;
                        flex: 1;
                        border-collapse: collapse;
                        table-layout: fixed;
                    }

                    th {
                        text-align: center;
                        vertical-align: bottom;
                    }

                    th, td:nth-child(1) {
                        font-weight: bold;
                        font-size: 0.85rem;
                        background: none;
                        border: none;
                        padding: 12px;
                    }

                    td:nth-child(1) {
                        text-align: right;
                        vertical-align: middle;
                    }

                    th:nth-child(1) {
                        text-align: right;
                        vertical-align: bottom;
                    }

                    .result-cell {
                        vertical-align: top;
                        text-align: left;
                        border: 1px solid #444;
                        padding: 2px;
                        font-family: Arial, sans-serif;
                        font-size: 0.85rem;
                        color: #1a1a1a;
                    }

                    .pass {
                        background-color: #a5d6a7;
                        vertical-align: top;
                        text-align: left;
                    }

                    .partial {
                        background-color: #fff176;
                    }

                    .fail {
                        background-color: #ef9a9a;
                        vertical-align: top;
                        text-align: left;
                    }

                    .none {
                        background-color: #f0f0f0;
                        vertical-align: top;
                        text-align: left;
                    }
                    
                    .version-badge {
                        display: inline-block;
                        font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
                        font-size: 0.85rem;
                        margin: 4px;
                        padding: 4px 10px;
                        border-radius: 8px;
                        background-color: #f0f0f0;
                        color: #1f2937;
                        font-weight: 500;
                        letter-spacing: 0.05em;
                    }
                    
                    .translucent-box {
                        background-color: rgba(255, 255, 255, 0.5); /* subtle dark overlay */
                        margin: 3px;
                        padding: 2px 4px;
                        border-radius: 2px;
                        display: flex;
                        align-items: flex-start;
                        gap: 5px;
                    }
                    
                    .translucent-box svg {
                        width: 1.2em;
                        height: 1.2em;
                        flex-shrink: 0;
                    }
                    
                    .translucent-box:nth-child(1) {
                        flex: 0 0 30px; /* fixed width */
                    }

                    .translucent-box:nth-child(2) {
                        flex: 1; /* takes remaining space */
                    }
                    
                    .result-icon {
                      width: 1em;
                      height: 1em;
                      vertical-align: -0.125em;
                    }
                </style>
            </head>
            <body>
            <h1>$title</h1>
            <table style="width:100%">
                <thead>
                <tr>
                    <th>Provider&nbsp;→<br/>↓&nbsp;Consumer<span style="color: rgba(0,0,0,0);">&nbsp;→</span></th>
                    ${htmlCells.first().joinToString("\n")}
                </tr>
                </thead>
                <tbody>
                ${htmlCells.drop(1).joinToString("\n") { it.joinToString("\n", prefix = "<tr>", postfix = "</tr>") }}
                </tbody>
            </table>
            </body>
            </html>
        """.trimIndent()
    }

    private fun htmlForTestResult(
        src: InteroperabilityMatrix.Cell,
    ): String {
        val passedList = src.passedList.sorted().joinToString(", ")
        val failedList = src.failedList.sorted().joinToString(", ")
        val missingResultList = src.missingList.sorted().joinToString(", ")
        val notImplementedList = src.noneList.sorted().joinToString(", ")

        return mutableListOf<String>().apply {
            if (src.failedList.isNotEmpty()) {
                if (src.verdict == Verdict.FAIL) {
                    add("""<div class="translucent-box"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-circle-x-icon lucide-circle-x"><circle cx="12" cy="12" r="10"/><path d="m15 9-6 6"/><path d="m9 9 6 6"/></svg><div>$failedList</div></div>""")
                } else {
                    add("""<div class="translucent-box"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-circle-check-icon lucide-circle-check"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg><div>$passedList</div></div>""")
                    add("""<div class="translucent-box"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-circle-alert-icon lucide-circle-alert"><circle cx="12" cy="12" r="10"/><line x1="12" x2="12" y1="8" y2="12"/><line x1="12" x2="12.01" y1="16" y2="16"/></svg><div>$failedList</div></div>""")
                }
            } else {
                if (src.verdict == Verdict.PASS) {
                    add("""<div class="translucent-box"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-circle-check-icon lucide-circle-check"><circle cx="12" cy="12" r="10"/><path d="m9 12 2 2 4-4"/></svg><div>$passedList</div></div>""")
                }
            }

            if (src.missingList.isNotEmpty()) {
                add("""<div class="translucent-box"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" class="lucide lucide-circle-question-mark result-icon" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"></circle><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3M12 17h.01"></path></svg><div>$missingResultList</div></div>""")
            }

            if (src.noneList.isNotEmpty()) {
                add("""<div class="translucent-box"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-circle-dot-icon lucide-circle-dot"><circle cx="12" cy="12" r="10"/><circle cx="12" cy="12" r="1"/></svg><div>$notImplementedList</div></div>""")
            }
        }.joinToString("")
    }

    private fun addZeroWidthSpace(text: String): String {
        return text.split(" ").joinToString(" ") {
            it.chunked(1).joinToString("\u200B")
        }
    }
}