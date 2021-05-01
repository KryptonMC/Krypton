# Krypton contribution guide

Hey there! Good to see you've decided to open this document! I hope you enjoy your stay, and I hope this proves useful to you.

First of all, thank you for considering contributing to Krypton! I really appreciate all who do! This project won't survive without
your contributions.

**Table of contents:**
* [Requirements](#requirements)

## Requirements

First and foremost, I assume that you already have your chosen IDE. If not, I can personally recommend [IntelliJ](https://www.jetbrains.com/idea/).

Secondly, you'll need a few tools to start making contributions. Most of these should come with your IDE, but some may not:
- Gradle
- Git
- Some form of Kotlin support / Kotlin plugin

If your IDE doesn't have support for Gradle, you can use the Gradle wrapper (`gradlew` or `gradlew.bat`), and for Git, you can
either use Git Bash (Windows/Mac), or just install the `git` command-line tool (Linux). Or, if you prefer GUIs, I can personally
recommend [GitKraken](https://www.gitkraken.com/).

Third and final, I recommend familiarising yourself with the code style of the project (you can see a few style hints in `.editorconfig`,
or you can just browse some files and have a look for yourself). We closely follow the official [Kotlin conventions](https://kotlinlang.org/docs/coding-conventions.html),
with the following constraints:
- All spacing must use the space character. No tab characters must be present in source files.
    - Empty lines should contain no spaces at all
- No trailing whitespace
- All API additions, and (preferably) all public server additions should be properly documented.
- All code should be free of magic values (generally hard coded numbers or strings that have no meaning and no purpose other
  than just making things work) if possible. If you have an addition that should be configurable, add it to the config. If your
  addition should be hard coded, use a variable, or better yet, a constant
    - For example:
      ```kotlin
        const val SOME_VALUE = 10

        // Usage
        if (something == SOME_VALUE) doSomething()
      ```
      is better than:
      ```kotlin
        if (something == 10) doSomething()
      ```

## Specific Information
### API

The API is heavily designed around the very popular Bukkit API, with some changes to make it much better to use and much
more Kotlin-friendly.

The following are examples of what I classify as "issues" (some may not see them as such), and how we attempt fix them:
- Bukkit is heavily dependent on statics and a late init singleton (the `Bukkit` class is the example here), which leads to
  messy code that often breaks the single responsibility principle (SRP). We attempt to solve this by removing the singleton
  entirely and requiring that you use the instance of the server given to you in your `PluginContext`, encouraging dependency
  injection as a better alternative.
- Bukkit is nearly (may even be by now) a decade old. In that time, Minecraft has undergone so many changes that it becomes
  difficult to maintain backwards compatibility, especially when vanilla does not. These attempts to maintain backwards
  compatibility lead to things like the legacy material system in the `Material` enum, as well as the use of legacy strings
  with legacy formatting codes. We solve this by promising that we will not attempt to maintain backwards compatibility if
  Minecraft introduces significant enough changes that it is worth us redesigning the API to support them.

In addition to this though, the API is also has a requirement to be as Java compatible as possible (as you can see from the
abundant use of annotations under the `kotlin.jvm` package). This is to ensure that users who choose to use Java instead of
Kotlin are not welcomed by an awful API that is not very idiomatic or very usable for them.

Furthermore, we generally follow what I will call here the "manager" pattern, which is that you create classes that hold and
represent data, and you also create managers for these classes that can be used to read and write these objects. A good
example of this is worlds, where you have the `World` interface that represents a loaded world, and then the `WorldManager`
to load and save these worlds.

And finally, the primary goal of the API is to provide an abstraction layer for plugins that is not complicated to understand,
and is abstract enough that it is **platform agnostic**. This means that everything, from your code to your comments, must be
as open as possible about what the intended behaviour of a specific variable or function is. For example, you may suggest that
a function may throw an error in a specific case, but you should not take this behaviour for granted, as even though the
implementation of this API is 99% of the time going to be Krypton, it may not always be.

### Server

The server is a bit more complex than the API. It does not have many of the strict requirements of the API, like Java
compatibility or abstraction layers for plugins, but it still does have some guidelines that you should at least attempt
to follow:
- Do remember that the server is technically public API, so please try not to leave public sections of the server
  undocumented where they could be ambiguous.
- Try to be very strict about what is public API and what is for internal use only. This helps eliminate ambiguity, and
  reduces the chance that a plugin that uses the server may accidentally call something it should not (for example, the
  `start` function in `KryptonServer`)

In terms of code, I generally aim for implementation packages to be the exact same as their API equivalents, excluding the
`api` part of the package (for example, the scheduling package in the API is `org.kryptonmc.krypton.api.scheduling`, and
the implementation package is `org.kryptonmc.krypton.scheduling`). This makes it easier to locate the implementations,
as the packages are very similar.

The "Notchian" server, as it is called by [wiki.vg](https://wiki.vg), or just the vanilla server, refers to the official
Minecraft server that is developed and distributed by Mojang. Whilst referencing material from this server is not recommended
due to possible copyright issues, it is however permitted (and I use it anyway, so it would be hypocritical for me to
not allow this). Please be careful though, as you may fall in to the trap of blindly copying vanilla code, where it may be
a better idea to rewrite it.

#### Packets

In most protocols, and especially this one, every message that comes in or goes out is called a "packet". Packets are used
to send and receive data to and from the client. More information on what packets there are and how they are formatted can
be found [here](https://wiki.vg/Protocol)

We use Netty's `ByteBuf` to read and write these packets, and we can do this through the use of extension functions. This
is why we do not require a helper class, such as vanilla's `FriendlyByteBuf`. All of the current `ByteBuf` extensions used
for both reading and writing data to and from `ByteBuf`s can be found in the `bytebufs.kt` file under the `util` package.
If you are adding a new form of type, it an extension to `ByteBuf` should be placed in `bytebufs.kt` that can read and/or
write the respective type to and/or from a `ByteBuf`. You can view other extensions in this file for a reference on how
you should do this.

In terms of packet handling, all encoding and decoding of messages should be done with Netty. All packets must implement
the `Packet` interface, though there are helper classes for specific states that can be found in the `state` package under
`packet`, and these should be preferred for their respective states (for example, all login packets extend the class
`LoginPacket`, which is an implementation of `Packet` with information specific to the login state).
In addition, packets are not queued for later use, they are handled immediately in the order they arrive when they are
called from a thread within Netty's asynchronous IO worker pool (you don't need to concern yourself with what this is or
how it works).

The actual reading and writing is done by overriding the `read` and `write` functions respectively. Note that these will,
by default, throw `UnsupportedOperationException`s if not overridden. You can register new inbound packets by adding a new
line in `PacketLoader`'s `loadAll` function, with something like `PacketState.STATE.registerPacketType(ID, ::PacketInSomething)`,
which will add the packet to the state's internal packet map, which is used by the Netty handlers to look up and find the
packet by the read ID.

The naming convention for packet classes is as follows: `Packet` + direction (`In` or `Out`) + name as documented on
[wiki.vg](https://wiki.vg/Protocol). For example, the clientbound statistics packet would be named `PacketOutStatistics`.

## Commits and pull requests

We use Git for version control, and Git uses commits to essentially describe the changes that occurred since the previous
commit. These commits can have a message associated with them, these messages should be a short and concise description
of the changes that you have made in it. If you have made multiple smaller changes, you can either:
- Describe the changes in the `CHANGELOG.md` file (preferred)
- Write a long description in the commit entailing what exactly was changed.

When you have finished with your contribution, you can submit a pull request to request that your changes from what is
called "downstream" (your fork) be merged into what is called "upstream" (the main project). We will then proceed to
review your pull request, and either approve what you have submitted, or we may request changes if we are not happy with
a specific thing (or many things) that you have done in your commit(s). Once we are fully happy with your changes, we will
merge your changes into our project. After this is complete, you're finally done! Feel free to then remove your fork if
you wish to do so.

## Other

If you have any suggestions that you think would make this contribution guide better, perhaps either making this more
informative, removing informality, fixing grammar or typos, or anything else, feel free to either open an issue or make
a pull request.

And finally, thank you for spending the time to read this contribution guide! Happy contributing!
