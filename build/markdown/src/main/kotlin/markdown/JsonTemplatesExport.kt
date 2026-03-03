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
        val featuresShort = src.validTestCases().joinToString(",\n") {
            """    {
      "testCaseId": "${it.id}"
    }"""
        }

        return Markdown.generate(false) {
            """
## Interoperability Matrix

The interoperability matrix of a certain Plug-a-thon event is built from a set of JSON files, some containing test
results and some containing library information valid at the time the tests have been conducted.

### Test Result File

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

### Library Features

Designates all roles, bindings, and features there were available for a certain Plug-a-thon.

- `<LIBRARY_ID>` is to be replaced with the identifier of the library
- `<VERSION>` is to be replaced with the applicable version; if none is available, leave empty. Example: semantic version `3.4.1`.
- `roles`: add at least `consumer` or `provider`, or both, to signify implementation of consumer/provider sides 
- `bindings`: `dpws` if the library provides a DPWS binding, `protosdc` if it provides a protoSDC binding, or both.
- `features`: list of all supported features; remove the ones you do not support (leave out `roles` property if the ones from above are both met, leave `roles` property empty, or omit the feature if no side implements the feature)

```json
{
  "id": "<LIBRARY_ID>",
  "version": "<VERSION>",
  "roles": [
    "consumer",
    "provider"
  ],
  "bindings": [
    "dpws",
    "protosdc"
  ],
  "features": [
$featuresShort
  ]
}
```

            """.trimIndent()
        }
    }

    private fun sdcLibrary(src: TestSequence): String {
        val featuresFull = src.validTestCases().joinToString(",\n") {
            """    {
      "testCaseId": "${it.id}",
      "roles": [
        "consumer",
        "provider"
      ]
    }"""
        }

        val featuresShort = src.validTestCases().joinToString(",\n") {
            """    {
      "testCaseId": "${it.id}"
    }"""
        }

        return Markdown.generate(false) {
            """
## SDC Library File

- `<LIBRARY_ID>` is to be replaced with an identifier for the library, all lowercase and dashes only; must be set and unique
- `<LIBRARY_NAME>` is to be replaced with the name of SDC library; must be set
- `license`: give markdown free text information as to if and by what terms the library can be acquired (not available at all, open source, proprietary) 
- `provider`: e.g., manufacturer name of the library
- `website`: a URL to guide people to more information
- `contact`: list of contact information (e.g. just names or links)
- `roles`: add at least `consumer` or `provider`, or both, to signify implementation of consumer/provider sides 
- `bindings`: `dpws` if the library provides a DPWS binding, `protosdc` if it provides a protoSDC binding, or both.
- `features`: list of all supported features; remove the ones you do not support (leave out `roles` property if the ones from above are both met, leave `roles` property empty, or omit the feature if no side implements the feature)

### Individual roles assignments (full)

Template for SDC implementations that support consumer and provider functionality only for certain features.

```json
{
  "id": "<LIBRARY_ID>",
  "name": "<LIBRARY_NAME>",
  "programmingLanguage": "",
  "license": "e.g. `Open Source`, `Proprietary license`, ...",
  "provider": "<PROVIDER_NAME>",
  "website": "<WEBSITE_URL>",
  "contact": [
    "[John Doe](http://example.com/contact/john.doe)",
    "Jane Roe"
  ],
  "roles": [
    "consumer",
    "provider"
  ],
  "bindings": [
    "dpws",
    "protosdc"
  ],
  "features": [
$featuresFull
  ]
}
```

### No individual role assignments

Template for SDC implementations that support consumer and provider functionality for all features.

```json
{
  "id": "<LIBRARY_ID>",
  "name": "<LIBRARY_NAME>",
  "programmingLanguage": "",
  "available": "e.g. `Open Source`, `Proprietary license`, ...",
  "manufacturer": "<MANUFACTURER_NAME>",
  "website": "<WEBSITE_URL>",
  "contact": [
    "[John Doe](http://example.com/contact/john.doe)",
    "Jane Roe"
  ],
  "roles": [
    "consumer",
    "provider"
  ],
  "bindings": [
    "dpws",
    "protosdc"
  ],
  "features": [
$featuresShort
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