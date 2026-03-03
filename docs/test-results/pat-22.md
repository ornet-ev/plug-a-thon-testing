---
icon: lucide/circle-check-big
hide:
- navigation
- toc
---


# PAT#22

??? Legend
    - :lucide-check: all featured tests succeeded
    - :lucide-x: all featured tests failed
    - :lucide-triangle-alert: some tests failed
    - :lucide-circle-question-mark: missing test results
    - empty: no tests executed
## Interoperability Matrix (DPWS binding)

<a href="javascript:window.history.back()" class="md-button">:lucide-arrow-big-left: Back</a>
<a href="pat-22-dpws.html" target="_blank" class="md-button" title="Print view in new window">:lucide-printer: Print Version</a>

| **Provider →**<br>**↓ Consumer** | C++ Dräger { title="Version: unknown" } | Lumos *1 { title="Version: unknown" } | SDCri { title="Version: 6.1.0" } | SdcLibrary .Net C# { title="Version: unknown" } | Vector SDC Stack { title="Version: unknown" } | protosdc-rs { title="Version: unknown" } | sdc11073 { title="Version: unknown" } | sdcX { title="Version: unknown" } |
| -------------------------------- | --------------------------------------- | ------------------------------------- | -------------------------------- | ----------------------------------------------- | --------------------------------------------- | ---------------------------------------- | ------------------------------------- | --------------------------------- |
| **Ascom** { title="Version: unknown" } | :lucide-check:{ title="All tests passed" } | :lucide-triangle-alert:{ title="Failed tests: 4h" } :lucide-circle-question-mark:{ title="Missing test results: 4f, 5a, 5b" } | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } |
| **C++ Dräger** { title="Version: unknown" } | :lucide-check:{ title="All tests passed" } | :lucide-triangle-alert:{ title="Failed tests: 6b" } :lucide-circle-question-mark:{ title="Missing test results: 5a, 5b, 6c, 6d, 6e, 6f" } | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } | :lucide-triangle-alert:{ title="Failed tests: 5a, 6b" } | :lucide-check:{ title="All tests passed" } |
| **CANoe** { title="Version: unknown" } | :lucide-check:{ title="All tests passed" } | :lucide-circle-question-mark:{ title="Missing test results: 5a, 5b, 6c, 6d, 6e, 6f" } | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } |
| **SDCri** { title="Version: 6.1.0" } | :lucide-check:{ title="All tests passed" } | :lucide-circle-question-mark:{ title="Missing test results: 4f, 4i, 5a, 5b, 6c, 6d, 6e, 6f" } | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } |
| **SdcLibrary .Net C#** { title="Version: unknown" } | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } |  |  | :lucide-check:{ title="All tests passed" } |
| **protosdc-rs** { title="Version: unknown" } | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } |
| **sdc11073** { title="Version: unknown" } | :lucide-triangle-alert:{ title="Failed tests: 2b" } |  |  |  |  |  |  |  |
| **sdcX** { title="Version: unknown" } | :lucide-check:{ title="All tests passed" } | :lucide-circle-question-mark:{ title="Missing test results: 1c, 1d, 4f, 4i, 5a, 5b, 6c, 6d, 6e, 6f" } | :lucide-check:{ title="All tests passed" } |  | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } | :lucide-check:{ title="All tests passed" } |

## Interoperability Matrix (protoSDC binding)

<a href="javascript:window.history.back()" class="md-button">:lucide-arrow-big-left: Back</a>
<a href="pat-22-protosdc.html" target="_blank" class="md-button" title="Print view in new window">:lucide-printer: Print Version</a>

| **Provider →**<br>**↓ Consumer** | protosdc-rs { title="Version: unknown" } | sdcX { title="Version: unknown" } |
| -------------------------------- | ---------------------------------------- | --------------------------------- |
| **protosdc-rs** { title="Version: unknown" } |  |  |
| **sdcX** { title="Version: unknown" } | :lucide-check:{ title="All tests passed" } |  |

