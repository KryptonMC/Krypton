# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.8] - 2021-03-03
### Added
- Legacy server list ping support
- Exception handling for connections
- Localisation support for join and leave messages
- Per-player gamemodes (W.I.P, is currently set to the world's default)

### Changed
- Handshake handling is now done in `PacketHandler`
- Disconnect handling - the disconnection packet now has to be sent
  separately before the connection is closed
- `PacketInfo` is now a data class
- Packet categorisation - Packets are now better categorised into
  their appropriate packages
- Packet sending - Packets sent to other connected players are now
  no longer instantiated for each individual connected player

### Removed
- Some more hard-coded values, such as the world's gamemode