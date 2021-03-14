# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.12] - 2021-03-12
### Added
- Cancellable event support using a BungeeCord-style event bus.
- Some basic events.
- Plugin scope, for executing the `initialize` method asynchronously.

### Changed
- Command scope now uses the same amount of threads as available processors
  on the CPU.
- Command errors are now appropriately handled.

### Removed
- Reactive event manager :(

## [0.11.2] - 2021-03-12
### Added
- Logging to files (creates a directory called logs and generates log
  files just like vanilla)
- Console input handling (you can now type commands into the console)
- Command handling! Typing commands in chat will now actually have them
  function as commands!
- Shutdown hook, so the server shuts down correctly.

## [0.11.1] - 2021-03-12
### Added
- Plugin loading! Plugins will now be loaded if their JARs are placed
  inside the `plugins` folder!

## [0.11] - 2021-03-12
### Added
- Brand new Krypton API! Still a work in progress, but the core of it is
  there!
- Support for reading boss bars from world files (`CustomBossEvents`)

### Changed
- `RegionManager` has now been merged into `KryptonWorldManager`
- Due to the new API, all classes that are implementations of their API
  counterparts have been renamed to now be prefixed with `Krypton` (e.g.
  `KryptonServer`, `KryptonWorldManager`, etc.)
- The project has now been split into two modules: api and server.

### Removed
- `Position`, `BlockPosition` and `ChunkPosition` have all been removed
  and replaced with `Vector`.

## [0.10] - 2021-03-07
### Added
- Player swing arm animation
- Entity actions (start/stop sneaking, start/stop sprinting, leave bed
  and start flying with elytra currently usable)
- Support for scoreboards, titles, action bars and tablist header & footer.
  You can't do anything with them yet, but they will be more usable in
  future versions. Also currently untested.

### Changed
- Sessions are now better managed by the `SessionManager`, which handles
  a lot of the packet sending and encryption & compression setup for
  sessions, instead of sessions handling that themselves (removal of god
  objects)
- Authentication is now entirely handled by `SessionService`, instead of
  it just wrapping the `MojangSessionService` and `Retrofit`
- `PacketHandler` has been reduced to a, well, packet handler, instead of
  distributing packets and authenticating users itself, further reducing
  god objects
- The second declare recipes packet that was being sent is now unlock recipes
  (like it should be), and unlock recipes is now working how it should.
- `Location` is now world bound, meaning it requires a world. This further
  separates it from `Position`, which is not world bound and doesn't allow
  for decimals in its coordinates, and `Vector`, which isn't world bound.

## [0.9.2] - 2021-03-06
### Changed
- Added some actual checking into namespaced key conversions
- Fixed issue with keys in join game containing invalid values, and
  added the worlds that should have been there

## [0.9.1] - 2021-03-05
### Added
- Some basic world generation configuration support

### Changed
- Join game now correctly specifies the hashed seed of the world, and
  the status (is debug/flat world)
- Chunk data's primary bit mask is finally no longer hard-coded, which
  means that now all worlds should be supported

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