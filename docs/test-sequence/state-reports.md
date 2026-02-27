---
icon: lucide/scroll-text
---

# State Reports

## Test case: 4a

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) produces at least 5 numeric metric updates in 30 seconds

## Test case: 4b

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) produces at least 5 string metric updates (StringMetric or EnumStringMetric) in 30 seconds

## Test case: 4c

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) produces at least 5 alert condition updates (AlertCondition or LimitAlertCondition) in 30 seconds

## Test case: 4d

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) produces at least 5 alert signal updates in 30 seconds

## Test case: 4e

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) provides alert system self checks in accordance to the periodicity defined in the MDIB (at most every 10 seconds). Note: This only applies to alert systems providing a self check period.

## Test case: 4f

### Preconditions

The network delay is capable of exchanging 10 messages per second in addition to the other updates

### Description

The [Reference Provider](../definitions/#reference-provider) provides 3 waveforms (RealTimeSampleArrayMetric) × 10 messages per second × 100 samples per message

## Test case: 4g

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) provides changes for the following components: at least 5 Clock or Battery object updates in 30 seconds (Component report); at least 5 MDS or VMD updates in 30 seconds (Component report)

## Test case: 4h

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) provides changes for the following operational states: at least 5 Operation updates in 30 seconds; enable/disable operations; some different than the ones mentioned above (Operational State Report)

## Test case: 4i

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) provides 1 waveform (RealTimeSampleArrayMetric) × 2 messages per second × 50 samples per message (reduced amount of messages per second to cover slow networks)
