package org.ornet.markdown

import org.ornet.TestSequence
import org.ornet.validTestCases

object JsonTemplatesExport {
    fun jsonTemplatesMd(
        src: TestSequence
    ): String {
        return StringBuilder().apply {
            appendLine(header())
            appendLine(testResults(src))
            appendLine(sdcLibrary(src))
        }.toString()
    }

    private fun testResults(src: TestSequence): String {
        val caseIds = src.validTestCases().map { it.id }.joinToString(",\n") { """        "$it"""" }
        return Markdown.generate(false) {
            """
## Test Result File

Use this JSON file template for the provision of test results.

- `<PAT_NUMBER>` is to be replaced with the plug-a-thon event at which the test results were recorded
- `<CONSUMER_LIBRARY_ID>` is to be replaced with the SDC library id of the consumer side
- `<PROVIDER_LIBRARY_ID>` is to be replaced with the SDC library id of the provider side
- `<VERDICT>` is to be replaced with the test verdict, one of `pass` or `fail`

```json
{
  "patNumber": <PAT_NUMBER>,
  "testResults": [
    {
      "consumerLibraryId": "<CONSUMER_LIBRARY_ID>",
      "providerLibraryId": "<PROVIDER_LIBRARY_ID>",
      "caseIds": [
$caseIds
      ],
      "verdict": "<VERDICT>"
    }
  ]
}
```
            """.trimIndent()
        }
    }

    private fun sdcLibrary(src: TestSequence): String {
        val features = src.validTestCases().joinToString(",\n") {
            """    {
      "testCaseId": "${it.id}",
      "roles": [
        "consumer",
        "provider"
      ]
    }"""
        }

        return Markdown.generate(false) {
            """
## SDC Library File

- `<LIBRARY_ID>` is to be replaced with an identifier for the library, all lowercase and dashes only; must be set and unique
- `<LIBRARY_NAME>` is to be replaced with the name of SDC library; must be set
- `roles`: add at least `consumer` or `provider`, or both, to signify implementation of consumer/provider sides 
- `available`: give markdown free text information as to if and by what terms the library can be acquired (not available at all, open source, proprietary) 
- `manufacturer`: manufacturer name of the library
- `website`: a URL to guide people to more information
- `contact`: list of contact information (e.g. just names or links)
- `features`: list of all supported features; remove the ones you do not support (leave out `roles` property if the ones from above are both met, leave `roles` property empty, or omit the feature if no side implements the feature)

```json
{
  "id": "<LIBRARY_ID>",
  "name": "<LIBRARY_NAME>",
  "roles": [
    "consumer",
    "provider"
  ],
  "programmingLanguage": "",
  "available": "e.g. `Open Source`, `Proprietary license`, ...",
  "manufacturer": "<MANUFACTURER_NAME>",
  "website": "<WEBSITE_URL>",
  "contact": [
    "[John Doe](http://example.com/contact/john.doe)",
    "Jane Roe"
  ],
  "features": [
$features
  ]
}
```
            """.trimIndent()
        }
    }

    private fun header(): String {
        return Markdown.generate {
            """
                ---
                icon: lucide/layout-template
                ---
                
                # JSON Templates
            """
        }
    }
}