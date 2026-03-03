package org.ornet.markdown

import org.ornet.Nomenclature
import org.ornet.TestSequence
import org.ornet.validTestCases

object NomenclatureExport {
    fun nomenclatureMd(
        src: Nomenclature,
    ): String {
        val nomenclatureTable = src.terms.map {
            "| ${it.contextSensitiveCode} | ${src.contextFreeCodeOffset + it.contextSensitiveCode} | ${it.description} |"
        }

        return Markdown.generate {
            """
                ---
                icon: lucide/file-digit
                ---

                # Nomenclature

                The MDIB is populated with private codes from the private partition of the `IEEE 11073-10101`:

                - Coding System OID (versionless): `${src.codingSystemId}`
                - Private Partition Context Free Code offset: `${src.contextFreeCodeOffset}`
                
                | Context-sensitive Code | Context-free Code | Description |
                |------------------------|-------------------|-------------|
                ${nomenclatureTable.joinToString("\n")}
            """
        }
    }
}