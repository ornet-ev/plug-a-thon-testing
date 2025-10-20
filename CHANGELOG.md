# Changelog

All notable changes to the MDIB will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

Each section shall contain a list of action items of the following format: `<brief one-sentence description of what has been done> (#\[<issue number]\(<URL>)).`

## [Unreleased]

### Fixed

- Fix subject in test 6f (was provider, changed to consumer). ([#13](https://github.com/ornet-ev/plug-a-thon-testing/issues/13))
- Add clarification notes to test 5b. ([#14](https://github.com/ornet-ev/plug-a-thon-testing/issues/14))
- Add clarification note to test 4e. ([#9](https://github.com/ornet-ev/plug-a-thon-testing/issues/9))

## [2.2.1] - 2025-04-08

### Fixed

- IEEE public codes now contain a valid coding system URI. ([#1](https://github.com/ornet-ev/plug-a-thon-testing/issues/1))

## [2.2.0] - 2025-02-27

### Added

- Fourth waveform that is updated twice per second (see handle `rtsa_metric_3.channel_1.vmd_0.mds_0`)

## [2.1.1] - 2024-11-21

### Fixed

- Added safety classification to operations set_value_0.sco.mds_0, set_string_0.sco.mds_0 and activate_1.sco.mds_0.

## [2.1.0] - 2024-11-13

Initial release based on test sequence 2, incorporating elements for testing of activate operations with parameters.
