---
icon: lucide/scroll-text
---

# Discovery

## Test case: 1a

### Preconditions

Requires a network supporting UDP multicast

### Description

The [Reference Provider](../definitions/#reference-provider) sends Hello messages in ad-hoc mode

## Test case: 1b

### Preconditions

Requires a network supporting UDP multicast

### Description

The [Reference Provider](../definitions/#reference-provider) answers to Probe and Resolve messages in ad-hoc mode

## Test case: 1c

### Preconditions

A [Discovery Proxy](../definitions/#discovery-proxy) is running and known to the [Reference Provider](../definitions/#reference-provider)

### Description

The [Reference Provider](../definitions/#reference-provider) sends Hello messages in managed mode by using the [Discovery Proxy](../definitions/#discovery-proxy)

## Test case: 1d

### Preconditions

A [Discovery Proxy](../definitions/#discovery-proxy) is running and known to the [Reference Provider](../definitions/#reference-provider)

### Description

The [Reference Provider](../definitions/#reference-provider) answers to Probe and Resolve messages in managed mode by using the [Discovery Proxy](../definitions/#discovery-proxy)
