# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.9] - 2021-03-04
### Added
- Basic TOML configuration file - you can now configure various options
  in the config.toml file, which will generate inside of the file that
  the JAR file is placed in.
- Support for reading the dimension codec from customly converted JSON
  files (found under registries/custom in resources, converted from
  the original SNBT files, which can be found [here](https://gist.githubusercontent.com/aramperes/44e2beefac9fe966177f2f28dd0136ab/raw/fedb31c32e27265fb916a68ad476470fc65631da/1-dimension_codec.snbt))
- Adventure! We have now fully switched to Adventure! See more
  [here](https://docs.adventure.kyori.net)

### Changed
- Fixed issue with dimension codec only sending one biome, thus making
  biomes invalid
- Continued cleaning things up.

### Removed
- No more system properties! Say goodbye to configuring worlds in system
  properties! You can now just specify the world name in the config
  and Krypton will find a world with that folder name

## [0.8.1] - 2021-03-03
### Changed
- Downgraded to Netty 4.1.59.Final from 5.0.0.Alpha2 due to issues
  with Epoll and KQueue, general stability, and that 5 has been
  discontinued by Netty
- `readAllAvailableBytes` (`ByteBuf` extension function) now checks
  if the buffer has a backing array, and copies the bytes into an
  array if it doesn't (to avoid issues with direct buffers not having
  backing arrays)

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