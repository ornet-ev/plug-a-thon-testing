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
        binding: Binding
    ): String {
        val interopMatrix = createInteroperabilityMatrix(
            src,
            testSequence,
            libFeatures
        )

        val libsForPat = libFeatures.associateBy { it.id }
        val versionHtml = libFeatures.associate { lib ->
            lib.id to lib.version.let {
                """<div class="version-badge">${it.ifEmpty { "n/a" }}</div>"""
            }
        }
        val libNames = libraries.associate { it.id to it.name }
        val sortedLibs = libraries.sortedBy { it.name }.map { libsForPat[it.id]!! }

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
            val row = listOf("<td><div>${libNames[consumerLib.id]!!}</div>${versionHtml[consumerLib.id]!!}</td>").toMutableList().also {
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
                        height: 100%;
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
                        font-size: 1em;
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
                        border: 1px solid #444;
                        padding: 2px;
                        font-family: Arial, sans-serif;
                        font-size: 1em;
                        color: #1a1a1a;
                    }

                    .pass {
                        background-color: #a5d6a7;
                        vertical-align: middle;
                        text-align: center;
                    }

                    .partial {
                        background-color: #fff176;
                    }

                    .fail {
                        background-color: #ef9a9a;
                        vertical-align: middle;
                        text-align: center;
                    }

                    .none {
                        background-color: #f0f0f0;
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
                    
                    .translucent-box:nth-child(1) {
                        flex: 0 0 1em; /* fixed width */
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
            <table style="width:100%; height: 100%">
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
        val failedList = src.failedList.sorted().joinToString(", ")
        val missingResultList = src.missingList.sorted().joinToString(", ")

        return mutableListOf<String>().apply {
            if (src.failedList.isNotEmpty()) {
                if (src.verdict == Verdict.FAIL) {
                    add("""<svg xmlns="http://www.w3.org/2000/svg" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" class="lucide lucide-x result-icon" viewBox="0 0 24 24"><path d="M18 6 6 18M6 6l12 12"></path></svg>""")
                } else {
                    add("""<div class="translucent-box"><div><svg xmlns="http://www.w3.org/2000/svg" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" class="lucide lucide-triangle-alert result-icon" viewBox="0 0 24 24"><path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3M12 9v4M12 17h.01"></path></svg></div><div>$failedList</div></div>""")
                }
            } else {
                if (src.verdict == Verdict.PASS) {
                    add("""<svg xmlns="http://www.w3.org/2000/svg" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" class="lucide lucide-check result-icon" viewBox="0 0 24 24"><path d="M20 6 9 17l-5-5"></path></svg>""")
                }
            }

            if (src.missingList.isNotEmpty()) {
                add("""<div class="translucent-box"><div><svg xmlns="http://www.w3.org/2000/svg" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" class="lucide lucide-circle-question-mark result-icon" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"></circle><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3M12 17h.01"></path></svg></div><div>$missingResultList</div></div>""")
            }
        }.joinToString("")
    }

}