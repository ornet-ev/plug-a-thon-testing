---
icon: lucide/scroll-text
---

# BICEPS Services Discovery and binding

## Test case: 2a

### Preconditions

None

### Description

The [Reference Provider](../definitions/#reference-provider) answers to TransferGet

## Test case: 2b

### Preconditions

The [Reference Consumer](../definitions/#reference-consumer) established at least one subscription to the [Reference Provider](../definitions/#reference-provider)

### Description

The [Reference Consumer](../definitions/#reference-consumer) renews at least one subscription once during the test phase; the [Reference Provider](../definitions/#reference-provider) grants subscriptions of at most 15 seconds (this allows for the [Reference Consumer](../definitions/#reference-consumer) to verify if auto-renew works)
