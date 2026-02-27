---
icon: lucide/layout-template
---

# JSON Templates

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
        "1a",
        "1b",
        "1c",
        "1d",
        "2a",
        "2b",
        "3a",
        "3b",
        "4a",
        "4b",
        "4c",
        "4d",
        "4e",
        "4f",
        "4g",
        "4h",
        "4i",
        "5a",
        "5b",
        "6b",
        "6c",
        "6d",
        "6e",
        "6f",
        "7a",
        "7b",
        "7c"
      ],
      "verdict": "<VERDICT>"
    }
  ]
}
```
## SDC Library File

- `<LIBRARY_ID>` is to be replaced with an identifier for the library, all lowercase and dashes only; must be set and unique
- `<LIBRARY_NAME>` is to be replaced with the name of SDC library; must be set
- `roles`: add at least `consumer` or `provider`, or both, to signify implementation of consumer/provider sides 
- `available`: give markdown free text information as to if and by what terms the library can be acquired (not available at all, open source, proprietary) 
- `manufacturer`: manufacturer name of the library
- `website`: a URL to guide people to more information
- `contact`: list of contact information (e.g. just names or links)
- `features`: list of all supported features; remove the ones you do not support (leave out `roles` property if the ones from above are both met, leave `roles` property empty, or omit the feature if no side implements the feature)

### Individual roles assignments (full)

Template for SDC implementations that support consumer and provider functionality only for certain features.

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
    {
      "testCaseId": "1a",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "1b",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "1c",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "1d",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "2a",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "2b",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "3a",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "3b",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "4a",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "4b",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "4c",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "4d",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "4e",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "4f",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "4g",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "4h",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "4i",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "5a",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "5b",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "6b",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "6c",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "6d",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "6e",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "6f",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "7a",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "7b",
      "roles": [
        "consumer",
        "provider"
      ]
    },
    {
      "testCaseId": "7c",
      "roles": [
        "consumer",
        "provider"
      ]
    }
  ]
}
```

### No individual role assignments

Template for SDC implementations that support consumer and provider functionality for all features.

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
    {
      "testCaseId": "1a"
    },
    {
      "testCaseId": "1b"
    },
    {
      "testCaseId": "1c"
    },
    {
      "testCaseId": "1d"
    },
    {
      "testCaseId": "2a"
    },
    {
      "testCaseId": "2b"
    },
    {
      "testCaseId": "3a"
    },
    {
      "testCaseId": "3b"
    },
    {
      "testCaseId": "4a"
    },
    {
      "testCaseId": "4b"
    },
    {
      "testCaseId": "4c"
    },
    {
      "testCaseId": "4d"
    },
    {
      "testCaseId": "4e"
    },
    {
      "testCaseId": "4f"
    },
    {
      "testCaseId": "4g"
    },
    {
      "testCaseId": "4h"
    },
    {
      "testCaseId": "4i"
    },
    {
      "testCaseId": "5a"
    },
    {
      "testCaseId": "5b"
    },
    {
      "testCaseId": "6b"
    },
    {
      "testCaseId": "6c"
    },
    {
      "testCaseId": "6d"
    },
    {
      "testCaseId": "6e"
    },
    {
      "testCaseId": "6f"
    },
    {
      "testCaseId": "7a"
    },
    {
      "testCaseId": "7b"
    },
    {
      "testCaseId": "7c"
    }
  ]
}
```
