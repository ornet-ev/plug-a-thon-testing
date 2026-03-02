---
icon: lucide/circle-check-big
hide:
- navigation
- toc
---


# PAT#22

## Interoperability Matrix

<a href="javascript:window.history.back()" class="md-button">:lucide-arrow-big-left: Back</a>
<a href="print.html" target="_blank" class="md-button" title="Print view in new window">:lucide-printer: Print Version</a>

| **Provider →**<br>**↓ Consumer** | C++ Dräger { title="Version: unknown" } | Lumos *1 { title="Version: unknown" } | SDCri { title="Version: 6.1.0" } | SdcLibrary .Net C# { title="Version: unknown" } | Vector SDC Stack { title="Version: unknown" } | protosdc-rs { title="Version: unknown" } | sdc11073 { title="Version: unknown" } | sdcX { title="Version: unknown" } |
| -------------------------------- | --------------------------------------- | ------------------------------------- | -------------------------------- | ----------------------------------------------- | --------------------------------------------- | ---------------------------------------- | ------------------------------------- | --------------------------------- |
| **Ascom** { title="Version: unknown" } | :lucide-check: | :lucide-triangle-alert: 4h<br>:lucide-circle-question-mark: 4f, 5a, 5b | :lucide-check: |  | :lucide-check: | :lucide-check: | :lucide-check: | :lucide-check: |
| **C++ Dräger** { title="Version: unknown" } | :lucide-check: | :lucide-triangle-alert: 6b<br>:lucide-circle-question-mark: 5a, 5b, 6c, 6d, 6e, 6f | :lucide-check: |  | :lucide-check: | :lucide-check: | :lucide-triangle-alert: 5a, 6b | :lucide-check: |
| **CANoe** { title="Version: unknown" } | :lucide-check: | :lucide-circle-question-mark: 5a, 5b, 6c, 6d, 6e, 6f | :lucide-check: |  | :lucide-check: | :lucide-check: | :lucide-check: | :lucide-check: |
| **SDCri** { title="Version: 6.1.0" } | :lucide-check: | :lucide-circle-question-mark: 4f, 4i, 5a, 5b, 6c, 6d, 6e, 6f | :lucide-check: |  | :lucide-check: | :lucide-check: |  | :lucide-check: |
| **SdcLibrary .Net C#** { title="Version: unknown" } | :lucide-check: |  | :lucide-check: |  | :lucide-check: |  |  | :lucide-check: |
| **protosdc-rs** { title="Version: unknown" } | :lucide-check: |  | :lucide-check: |  | :lucide-check: | :lucide-check: |  | :lucide-check: |
| **sdc11073** { title="Version: unknown" } | :lucide-triangle-alert: 2b |  |  |  |  |  |  |  |
| **sdcX** { title="Version: unknown" } | :lucide-check: | :lucide-circle-question-mark: 1c, 1d, 4f, 4i, 5a, 5b, 6c, 6d, 6e, 6f | :lucide-check: |  | :lucide-check: | :lucide-check: | :lucide-check: | :lucide-check: |
??? Legend
    - :lucide-check: all featured tests succeeded
    - :lucide-x: all featured tests failed
    - :lucide-triangle-alert: some tests failed
    - :lucide-circle-question-mark: missing test results
    - empty: no tests executed