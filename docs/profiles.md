---
icon: lucide/flask-conical
---

# Profiles

## Discovery

| ID | Precondition | Description |
| -- | ----------- | ----------- |
| 1a | Requires a network supporting UDP multicast | The [Reference Provider](definitions.md#reference-provider) sends Hello messages in ad-hoc mode |
| 1b | Requires a network supporting UDP multicast | The [Reference Provider](definitions.md#reference-provider) answers to Probe and Resolve messages in ad-hoc mode |
| 1c | A [Discovery Proxy](definitions.md#discovery-proxy) is running and known to the [Reference Provider](definitions.md#reference-provider) | The [Reference Provider](definitions.md#reference-provider) sends Hello messages in managed mode by using the [Discovery Proxy](definitions.md#discovery-proxy) |
| 1d | A [Discovery Proxy](definitions.md#discovery-proxy) is running and known to the [Reference Provider](definitions.md#reference-provider) | The [Reference Provider](definitions.md#reference-provider) answers to Probe and Resolve messages in managed mode by using the [Discovery Proxy](definitions.md#discovery-proxy) |

## BICEPS Services Discovery and binding

| ID | Precondition | Description |
| -- | ----------- | ----------- |
| 2a | — | The [Reference Provider](definitions.md#reference-provider) answers to TransferGet |
| 2b | The [Reference Consumer](definitions.md#reference-consumer) established at least one subscription to the [Reference Provider](definitions.md#reference-provider) | The [Reference Consumer](definitions.md#reference-consumer) renews at least one subscription once during the test phase; the [Reference Provider](definitions.md#reference-provider) grants subscriptions of at most 15 seconds (this allows for the [Reference Consumer](definitions.md#reference-consumer) to verify if auto-renew works) |

## Request Response

| ID | Precondition | Description |
| -- | ----------- | ----------- |
| 3a | — | The [Reference Provider](definitions.md#reference-provider) answers to GetMdib |
| 3b | — | The [Reference Provider](definitions.md#reference-provider) answers to GetContextStates with at least one location context state |

## State Reports

| ID | Precondition | Description |
| -- | ----------- | ----------- |
| 4a | — | The [Reference Provider](definitions.md#reference-provider) produces at least 5 numeric metric updates in 30 seconds |
| 4b | — | The [Reference Provider](definitions.md#reference-provider) produces at least 5 string metric updates (StringMetric or EnumStringMetric) in 30 seconds |
| 4c | — | The [Reference Provider](definitions.md#reference-provider) produces at least 5 alert condition updates (AlertCondition or LimitAlertCondition) in 30 seconds |
| 4d | — | The [Reference Provider](definitions.md#reference-provider) produces at least 5 alert signal updates in 30 seconds |
| 4e | — | The [Reference Provider](definitions.md#reference-provider) provides alert system self checks in accordance to the periodicity defined in the MDIB (at most every 10 seconds). *Note: This only applies to alert systems providing a self check period.* |
| 4f | The network delay is capable of exchanging 10 messages per second in addition to the other updates | The [Reference Provider](definitions.md#reference-provider) provides 3 waveforms (RealTimeSampleArrayMetric) × 10 messages per second × 100 samples per message |
| 4g | — | The [Reference Provider](definitions.md#reference-provider) provides changes for the following components: at least 5 Clock or Battery object updates in 30 seconds (Component report); at least 5 MDS or VMD updates in 30 seconds (Component report) |
| 4h | — | The [Reference Provider](definitions.md#reference-provider) provides changes for the following operational states: at least 5 Operation updates in 30 seconds; enable/disable operations; some different than the ones mentioned above (Operational State Report) |
| 4i | — | The [Reference Provider](definitions.md#reference-provider) provides 1 waveform (RealTimeSampleArrayMetric) × 2 messages per second × 50 samples per message (reduced amount of messages per second to cover slow networks) |

## Description Modifications

| ID | Precondition | Description |
| -- | ----------- | ----------- |
| 5a | — | The [Reference Provider](definitions.md#reference-provider) produces at least 1 update every 10 seconds comprising: update alert condition concept description of Type: change at least the content of the first localized text of one alert condition; update alert condition cause-remedy information: change at least the content of the first localized text of either cause or remedy texts of one alert condition; update Unit of measure (metrics): change at least the code of the unit of measure of one metric |
| 5b | — | The [Reference Provider](definitions.md#reference-provider) produces at least 1 insertion followed by a deletion every 10 seconds comprising: insert a VMD including Channels including metrics (inserted VMDs/Channels/Metrics are required to have a new handle assigned on each insertion such that containment tree entries are not recycled); remove the VMD. *Note: According to [R1008](https://profiles.ihe.net/DEV/SDPi/index.html#r1008), insertion and deletion must not be put into the same report. The provider may send updates in between insertion and deletion cycles.* |

## Operation Invocation

| ID | Precondition | Description |
| -- | ----------- | ----------- |
| ~~6a~~ | — | *(deprecated)* |
| 6b | — | The [Reference Consumer](definitions.md#reference-consumer) invokes SetContextState: payload 1 patient context; context state is added to the MDIB including context association and validation; if there is an associated context already, the [Reference Provider](definitions.md#reference-provider) disassociates that context; handle and version information is generated by the [Reference Provider](definitions.md#reference-provider); in order to avoid infinite growth of patient contexts, older contexts are allowed to be removed from the MDIB (when ContextAssociation is `No`); the [Reference Provider](definitions.md#reference-provider) successfully concludes the invocation |
| 6c | — | The [Reference Consumer](definitions.md#reference-consumer) invokes SetValue: the [Reference Provider](definitions.md#reference-provider) immediately responds with `Fin`; the [Reference Provider](definitions.md#reference-provider) sends `Fin` as a report in addition to the response |
| 6d | — | The [Reference Consumer](definitions.md#reference-consumer) invokes SetString: the [Reference Provider](definitions.md#reference-provider) initiates a transaction that sends `Wait`, `Start` and `Fin` |
| 6e | — | The [Reference Consumer](definitions.md#reference-consumer) invokes SetMetricStates: payload 2 metric states (settings; consider alert limits); the [Reference Provider](definitions.md#reference-provider) immediately responds with `Fin`; action: the [Reference Provider](definitions.md#reference-provider) alters values of given metrics |
| 6f | — | The [Reference Consumer](definitions.md#reference-consumer) invokes Activate: payload 3 arguments; the [Reference Provider](definitions.md#reference-provider) immediately responds with `Fin`; action: the [Reference Provider](definitions.md#reference-provider) accepts 3 arguments, concatenates them and writes them to the operation target's metric value |

## Localization Service

| ID | Precondition | Description |
| -- | ----------- | ----------- |
| 7a | — | The [Reference Consumer](definitions.md#reference-consumer) requests GetSupportedLanguages; the [Reference Provider](definitions.md#reference-provider) answers with GetSupportedLanguagesResponse containing all languages currently provided by the [Localized Text Database](definitions.md#localized-text-database) (V2: `en-US`, `de`, `el-GR`, `zh-CN`) |
| 7b | Test 7a succeeded, list of languages is available | For each language, the [Reference Consumer](definitions.md#reference-consumer) requests GetLocalizedText containing the language. The [Reference Provider](definitions.md#reference-provider) answers with GetLocalizedTextResponse containing all texts of the given language and version. The [Reference Consumer](definitions.md#reference-consumer) verifies that the result set is not empty. *Note: As the [Reference Provider](definitions.md#reference-provider) is allowed to change localized texts, the actual result set is unknown and cannot be verified against the [Localized Text Database](definitions.md#localized-text-database).* |
| 7c | Test 7a succeeded, list of languages is available | For each language, the [Reference Consumer](definitions.md#reference-consumer) requests GetLocalizedText containing the language and a version of 1. The [Reference Provider](definitions.md#reference-provider) answers with GetLocalizedTextResponse containing at least all texts from the [Localized Text Database](definitions.md#localized-text-database) corresponding to the requested language and version. *Note: Requesting the latest version can result in a different answer as the [Reference Provider](definitions.md#reference-provider) may change texts at any time.* |
