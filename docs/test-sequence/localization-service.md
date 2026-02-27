---
icon: lucide/scroll-text
---

# Localization Service

## Test case: 7a

### Preconditions

None

### Description

The [Reference Consumer](../definitions/#reference-consumer) requests GetSupportedLanguages; the [Reference Provider](../definitions/#reference-provider) answers with GetSupportedLanguagesResponse containing all languages currently provided by the Localized Text Database (V2: en-US, de, el-GR, zh-CN)

## Test case: 7b

### Preconditions

Test 7a succeeded, list of languages is available

### Description

For each language, the [Reference Consumer](../definitions/#reference-consumer) requests GetLocalizedText containing the language. The [Reference Provider](../definitions/#reference-provider) answers with GetLocalizedTextResponse containing all texts of the given language and version. The [Reference Consumer](../definitions/#reference-consumer) verifies that the result set is not empty. Note: As the [Reference Provider](../definitions/#reference-provider) is allowed to change localized texts, the actual result set is unknown and cannot be verified against the Localized Text Database.

## Test case: 7c

### Preconditions

Test 7a succeeded, list of languages is available

### Description

For each language, the [Reference Consumer](../definitions/#reference-consumer) requests GetLocalizedText containing the language and a version of 1. The [Reference Provider](../definitions/#reference-provider) answers with GetLocalizedTextResponse containing at least all texts from the Localized Text Database corresponding to the requested language and version. Note: Requesting the latest version can result in a different answer as the [Reference Provider](../definitions/#reference-provider) may change texts at any time.
