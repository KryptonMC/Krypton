# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.16.1] - 2021-03-29
### Changed
- Moved player data persistence into its own `PlayerDataManager`
- Fixed issues with join messages
- Replaced imperative `for` each loops with the superior declarative `forEach` operation
- Probably a premature optimisation, but avoided instantiation of `NamespacedKey` in places where it is
  constant
- Skin metadata is now properly updated when the client sends a client settings packet
  
### Removed
- `PacketState`'s ID field is gone, and its references (literally none) have been replaced with the
  enum's ordinal

## [0.16] - 2021-03-28
### Added
- Player data persistence (now uses <player>.dat files to save and load player data)
- Console now has three simple registered translation keys, to allow sending join, leave and chat
  messages to the console without extra work (`TranslationRegister`)
- Rain ticking (now checks if there is rain time and counts it down every tick, and stops raining when
  the counter reaches 0)

### Changed
- Fixed `LegacyQueryHandler`'s copied `ByteBuf` sometimes not getting released before it was
  garbage collected
- Rotation update packets now actually update the player's stored position, rather than just
  sending it to the other players
- `start` in `KryptonServer` is now `internal` (for obvious reasons)
- `PacketOutJoinGame` now accepts a previous gamemode and a dimension key from the player's data,
  instead of using previously hard coded -1 and overworld values respectively
- Scoreboard functions now (mostly) do what they are supposed to
- Unused registry entries are now commented out, to avoid us storing way more data than we need to
- Bumped Adventure version to 4.7.0 and made it a shared constant in the server (Netty's version is
  now also a shared constant)
- Terminal console now uses `server.isRunning` to determine if the server is running, rather than a
  hard coded `true`

### Removed
- `Item` class (internally, left over from when I thought you needed a class per item)
- `teleportId` property from `Session` (no need to store it, as we only read it once)

## [0.15.1] - 2021-03-27
### Added
- Players now have a `World`, which is the world they are currently in.

### Changed
- Fixed time update packets not filtering by world, meaning worlds could send time packets to
  players in other worlds
- Moved packet state change below world and location initialisation to avoid the chance that
  the variable could not be initialised when we access it in the ticking mechanism.

## [0.15] - 2021-03-27
### Added
- Time ticking (the server now sends out a time update packet every second, and the time is
  updated every tick). This is the start of ticking for Krypton.
- Watchdog thread, to make sure the tick thread doesn't die. The threshold for what this thread
  considers "death" is configurable in the main configuration
- Suggestion support (tab completion)
- Warning when memory is 512 MB or below (vanilla threshold)
- Built-in stop command to stop the server.
- Vanilla's two uncaught exception handlers, to catch and log exceptions from threads that throw
  exceptions in execution.

### Changed
- Updated `CommandManager`'s `register` functions that take Brigadier types to accept nodes
  instead of builders
- `Sender` is now identified (extends `Identified`, meaning it has an `Identity`, or a UUID)
- `WorldBorder` now has a `World` property again.
- Overrode permission checks for the console to force them to always succeed, as we are god
  and permission checks are for peasants :)
- Added `unregisterAll` function to the event bus, to allow
- Properly disconnect all connected players when the server stops.

## [0.14.1] - 2021-03-24
### Changed
- Fixed Krypton JAR not working properly due to the `Log4J2Plugins.dat` file not being merged from
  Log4J 2 and Minecrell's TerminalConsoleAppender (see [here](https://github.com/Minecrell/TerminalConsoleAppender/issues/15))
- Changed log level of world loading to INFO to allow it to be seen in production (not that
  Krypton is production ready yet though)

## [0.14] - 2021-03-21
### Added
- Support for more of Adventure's `Audience` methods, such as `sendActionBar`,
  `sendPlayerListHeaderAndFooter`, `showTitle`, `clearTitle` and `resetTitle`
- Better documentation for some of the `data class`es in the API by using `@param` to describe
  their constructor parameters
- Default parameter for `title` in the `PacketOutTitle` packet to allow us to send action bars
  without having to provide an empty title ourselves (the title isn't sent anyway if we use the
  set action bar `TitleAction`)

### Changed
- Fixed issue with player joining loading so many chunks that you would find over 50,000 sections
  and around 7-10 million `Long`s being allocated on the heap by only having the server load the
  chunks required by the client.
- Config file format is now HOCON instead of TOML.
- Errors with plugin loading and instantiation are now better handled and better described.
- `PacketOutAbilities` now uses the API `Abilities` class rather than the internal one.
- The metadata writing functions now have a shared constant for the ending index `0xFF`
- Cleaned up the `KryptonEventBus` to remove an unnecessary `do while` and replace it with a `for`,
  also cleaned up a few things that were remnants of messy Java code.
- Switched to Gradle's Kotlin DSL.

### Removed
- Removed `id` properties from enums where their values were the same as the `ordinal`, to preserve
  very minor memory space (micro optimisation I know)
- Removed internal `Abilities` class (replaced with API one)

## [0.13.9] - 2021-03-21
### Added
- Terminal console appender, to add support for legacy section formatting codes in the console
- Serialisers for all of Brigadier's argument type parsers

### Changed
- Fixed issue with arguments not being registered in the Brigadier argument builders when commands
  were registered, and so commands with more than just the initial command were not being parsed.
- Permission check event only firing when a command performs a permission check when it needs to be
  fired every time `hasPermission` is called
- Better handling of `CommandSyntaxException`s from Brigadier, meaning you now better see what is
  actually wrong with the command you typed.
- Fixed issues with the scheduler not shutting down properly (causing the plugin manager to never be
  able to shut down plugins either) due to a `ConcurrentModificationException` as I was not using a
  `ConcurrentHashMap` and `KeySetView` (`ConcurrentHashMap.newKeySet()`) for storing tasks.

## [0.13.8] - 2021-03-20
### Changed
- Made locale a nullable var rather than a lateinit var to avoid issues where it would not be set and
  a plugin would attempt to access it, resulting in an error.

## [0.13.7] - 2021-03-20
### Added
- Console's sender object is now exposed to the API

## [0.13.6] - 2021-03-20
### Added
- Ability to manipulate permission checks (change the result of them)

## [0.13.5] - 2021-03-20
### Added
- Quit event (no, it can't be cancelled)

## [0.13.4] - 2021-03-20
### Added
- Permission check event, which is fired every time a permission check is made.

### Changed
- Permissions are now mapped to boolean values, to allow for them to be tristate (true, false or unset).

## [0.13.3] - 2021-03-20
### Changed
- Priority values are now bytes to avoid confusion because the event bus ignores any values outside the
  range of a byte

## [0.13.2] - 2021-03-20
### Changed
- Priorities are now in the correct order (MAXIMUM first, NONE last)

## [0.13.1] - 2021-03-20
### Added
- Calling of the PluginMessageEvent and MoveEvent
- Optional reason for cancellation for the login and join events (defaults to the Mojang default)
- Latency update packet to the login sequence, as denoted in https://wiki.vg/Protocol_FAQ#What.27s_the_normal_login_sequence_for_a_client.3F

### Changed
- For now, MoveEvent is no longer cancellable, as I do not want to cause any issues.

## [0.13] - 2021-03-19
### Added
- Simple scheduling using an executor service.

## [0.12.1] - 2021-03-19
### Added
- Support for appending URLs to plugins' class loaders at runtime.

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