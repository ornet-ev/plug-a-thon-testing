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
import org.ornet.sortAndConcatenate

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
            libraries,
            libFeatures
        )

        val libNames = libraries.associate { it.id to it.name }

        val consumerLibsMap = interopMatrix.consumers.associateBy { it.id }
        val providerLibsMap = interopMatrix.providers.associateBy { it.id }

        val implementedFeatures = libFeatures.map { it.id }.associateWith { consumerLibsMap[it] to providerLibsMap[it] }
        val implementedFeaturesList = implementedFeatures.map { lib ->
            val consumerFeatures =
                sortAndConcatenate(lib.value.first?.features?.filter { it.roles!!.isNotEmpty() }
                    ?.map { it.testCaseId }).let {
                    if (it.isNotEmpty()) {
                        """<span class="lib-name">${libNames[lib.key]} Consumer:</span> <span class="features">$it</span>"""
                    } else {
                        ""
                    }
                }
            val providerFeatures =
                sortAndConcatenate(lib.value.second?.features?.filter { it.roles!!.isNotEmpty() }
                    ?.map { it.testCaseId }).let {
                    if (it.isNotEmpty()) {
                        """<span class="lib-name">${libNames[lib.key]} Provider:</span> <span class="features">$it</span>"""
                    } else {
                        ""
                    }
                }

            listOf(consumerFeatures, providerFeatures).filterNot { it.isEmpty() }.joinToString(separator = "<br/>\n")
        }.joinToString(separator = "<br/>\n") {
            implementedFeaturesListItem(it)
        }

        val libsForPat = libFeatures.associateBy { it.id }
        val versionHtml = libFeatures.associate { lib ->
            lib.id to lib.version.let {
                val zwAdded = addZeroWidthSpace(it)
                """<div class="version-badge">${zwAdded.ifEmpty { "n/a" }}</div>"""
            }
        }

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
                    
                    div.translucent-box {
                        background-color: rgba(255, 255, 255, 0.5); /* subtle dark overlay */
                        margin: 3px;
                        padding: 2px 4px;
                        border-radius: 2px;
                        display: flex;
                        align-items: flex-start;
                        gap: 5px;
                    }
                    
                    div.translucent-box svg {
                        width: 1.2em;
                        height: 1.2em;
                        flex-shrink: 0;
                    }
                    
                    div.translucent-box:nth-child(1) {
                        flex: 0 0 30px; /* fixed width */
                    }

                    div.translucent-box:nth-child(2) {
                        flex: 1; /* takes remaining space */
                    }
                   
                    .result-icon {
                      width: 1em;
                      height: 1em;
                      vertical-align: -0.125em;
                    }
                    
                    .adjunct-list {
                        margin-top: 24px;
                        padding: 12px 16px;
                        border: 1px solid #ccc;
                        border-radius: 6px;
                        display: inline-block;
                        background: #fafafa;
                    }
            
                    .adjunct-list-title {
                        display: block;
                        margin-bottom: 10px;
                        font-size: 0.85rem;
                        font-weight: bold;
                    }
            
                    .adjunct-list-list {
                        list-style: none;
                        display: flex;
                        flex-direction: column;
                        gap: 6px;
                    }
            
                    .adjunct-list-item {
                        display: flex;
                        align-items: center;
                        gap: 8px;
                        font-size: 0.85rem;
                    }
            
                    .adjunct-list-item svg {
                        flex-shrink: 0;
                        width: 1.2em;
                        height: 1.2em;
                    }
                    
                    .lib-name {
                        font-weight: bold;
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
            <div class="adjunct-list">
                <strong class="adjunct-list-title">Legend</strong>
                <ul class="adjunct-list-list">
                    <li class="adjunct-list-item">
                        ${SvgIcons.PASSED}
                        <span>Featured tests succeeded</span>
                    </li>
                    <li class="adjunct-list-item">
                        ${SvgIcons.FAILED}
                        <span>Featured tests failed</span>
                    </li>
                    <li class="adjunct-list-item">
                        ${SvgIcons.MISSING}
                        <span>Missing test results</span>
                    </li>
                    <li class="adjunct-list-item">
                        ${SvgIcons.NOT_IMPLEMENTED}
                        <span>Tests not implemented (either provider or consumer side)</span>
                    </li>
                    <li class="adjunct-list-item">
                        ${SvgIcons.NO_TESTS_EXECUTED}
                        <span>No tests executed</span>
                    </li>
                </ul>
            </div>
            <div class="adjunct-list">
                <strong class="adjunct-list-title">Implemented features</strong>
                $implementedFeaturesList
            </div>
            </body>
            </html>
        """.trimIndent()
    }

    private fun htmlForTestResult(
        src: InteroperabilityMatrix.Cell,
    ): String {
        val passedList = sortAndConcatenate(src.passedList)
        val failedList = sortAndConcatenate(src.failedList)
        val missingResultList = sortAndConcatenate(src.missingList)
        val notImplementedList = sortAndConcatenate(src.noneList)

        return mutableListOf<String>().apply {
            if (src.failedList.isEmpty() && src.missingList.isEmpty() && src.passedList.isEmpty()) {
                add("""<div class="translucent-box">${SvgIcons.NO_TESTS_EXECUTED}<div></div></div>""")
            }
            if (src.failedList.isNotEmpty()) {
                if (src.verdict == Verdict.FAIL) {
                    add("""<div class="translucent-box">${SvgIcons.FAILED}<div>$failedList</div></div>""")
                } else {
                    add("""<div class="translucent-box">${SvgIcons.PASSED}<div>$passedList</div></div>""")
                    add("""<div class="translucent-box">${SvgIcons.FAILED}<div>$failedList</div></div>""")
                }
            } else {
                if (passedList.isNotEmpty()) {
                    add("""<div class="translucent-box">${SvgIcons.PASSED}<div>$passedList</div></div>""")
                }
            }

            if (src.missingList.isNotEmpty()) {
                add("""<div class="translucent-box">${SvgIcons.MISSING}<div>$missingResultList</div></div>""")
            }

            if (src.noneList.isNotEmpty()) {
                add("""<div class="translucent-box">${SvgIcons.NOT_IMPLEMENTED}<div>$notImplementedList</div></div>""")
            }
        }.joinToString("")
    }

    private fun addZeroWidthSpace(text: String): String {
        return text.split(" ").joinToString(" ") {
            it.chunked(1).joinToString("\u200B")
        }
    }

    private fun implementedFeaturesListItem(text: String) = """
                <ul class="adjunct-list-list">
                    <li class="adjunct-list-item">
                        ${SvgIcons.FEATURES}
                        <span>$text</span>
                    </li>
                </ul>
        """.trimIndent()
}