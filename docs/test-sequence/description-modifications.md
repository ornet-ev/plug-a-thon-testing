---
icon: lucide/scroll-text
---

# Description Modifications

## Test case: 5a

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) produces at least 1 update every 10 seconds comprising: update alert condition concept description of Type: change at least the content of the first localized text of one alert condition; update alert condition cause-remedy information: change at least the content of the first localized text of either cause or remedy texts of one alert condition; update Unit of measure (metrics): change at least the code of the unit of measure of one metric

## Test case: 5b

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) produces at least 1 insertion followed by a deletion every 10 seconds comprising: insert a VMD including Channels including metrics (inserted VMDs/Channels/Metrics are required to have a new handle assigned on each insertion such that containment tree entries are not recycled); remove the VMD. Note: According to R1008, insertion and deletion must not be put into the same report. The provider may send updates in between insertion and deletion cycles.
