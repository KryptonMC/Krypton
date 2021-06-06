# Krypton contribution guide

Hey there! Good to see you've decided to open this document! I hope you enjoy your stay, and I hope this proves useful to
you.

First of all, thank you for considering contributing to Krypton! I really appreciate all who do! This project may not
survive without contributions.

**Table of contents:**
* [Requirements](#requirements)
* [Specific Information](#specific-information)
    * [API](#api)
    * [Server](#server)
        * [Packets](#packets)
* [Formatting](#formatting)
* [Other](#other)

## Requirements

First and foremost, I assume that you already have your chosen IDE. If not, I can personally recommend [IntelliJ](https://www.jetbrains.com/idea/).

Secondly, you'll need a few tools to start making contributions. Most of these should come with your IDE, but some may not:
* Gradle
* Git
* Some form of Kotlin support / Kotlin plugin

If your IDE doesn't have support for Gradle, you can use the Gradle wrapper (`gradlew` or `gradlew.bat`), and for Git, you can
either use Git Bash (Windows/Mac), or just install the `git` command-line tool (Linux). Or, if you prefer GUIs, I can personally
recommend [GitKraken](https://www.gitkraken.com/).

Third and final, I recommend familiarising yourself with the code style of the project (you can see a few style hints in `.editorconfig`,
or you can just browse some files and have a look for yourself). We closely follow the official [Kotlin conventions](https://kotlinlang.org/docs/coding-conventions.html),
with the following constraints:
* All spacing must use the space character. No tab characters must be present in source files.
    * Empty lines should contain no spaces at all
* No trailing whitespace
* All API additions, and (preferably) all public server additions should be properly documented (for maintainability).
* All code should be free of magic values (generally hard coded numbers or strings that have no meaning and no purpose other
  than just making things work) if possible. If you have an addition that should be configurable, add it to the config. If your
  addition should be hard coded, use a variable, or better yet, a constant
    * For example:
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

The API is designed around that of it's predecessors. It is primarily designed to be concise enough to not be complex for
beginners to pick up, but also be advanced enough that more experienced developers can do a lot with it. Some of the APIs
we take ideas from include Bukkit, Sponge, and Velocity, however I believe we improve on some of their methods.

It is very heavily reliant on dependency injection. There are no static singletons in Krypton, and there should never
be, as they are messy and hacky to work with. The way we do dependency injection is with a framework called
[Guice](https://github.com/google/guice), which is quite straight forward to learn, and allows for very easy injection,
and helps to reduce boilerplate.

It should also be designed to evolve nicely with Minecraft, and be welcoming to new additions and old deletions, making
it easy for developers to update. However, this is not saying that backwards compatibility should be prioritised over the
quality of the API. Essentially, don't make big unnecessary changes to the structure or functionality, but don't hold back
updating the API to handle new features that Minecraft brings just because they may break backwards compatibility.

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
to follow.

The server is not designed to maintain backwards compatibility. As of version 0.23, it is no longer public API, and
maintaining binary compatibility on the server's end should not be a priority at all. This change to stop the server
being public API also means that the server will follow a different versioning policy, and will no longer use semantic
versioning. We decided to make this change to allow greater freedom on the backend.

In terms of code, I generally aim for implementation packages to be similar to their API equivalents, generally replacing
the `api` part of the package with `krypton` (for example, the scheduling package in the API is `org.kryptonmc.api.scheduling`,
and the implementation package is `org.kryptonmc.krypton.scheduling`). This makes it much easier for people reading the
code to find the code that actually backs the abstraction layer (API), should they wish to know more about the actual
functionality. In addition, I find this is much more uniform, and keeping to this convention makes the code easier to
navigate, since you know where something is most likely to be.

The "Notchian" server, as it is called by [wiki.vg](https://wiki.vg), or just the vanilla server, refers to the official
Minecraft server that is developed and distributed by Mojang. Whilst referencing material from this server is not recommended
due to possible copyright issues, it is permitted (and I use it anyway, so it would be hypocritical for me to not allow
this). Please be careful though, as you may fall in to the trap of blindly copying vanilla code, where it may be a better
idea to rewrite it.

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
called from a thread within Netty's asynchronous IO worker pool. You should therefore exercise caution when creating things
that depend on other players, for example if a player needs to see another, as you may accidentally fall in to the trap
of assuming that something will always be initialised by the time you access it, which may cause a race condition. See
[issue #29](https://github.com/KryptonMC/Krypton/issues/29) for an example of how badly it can get if you do it wrong.

The actual reading and writing is done by overriding the `read` and `write` functions respectively. Note that these will,
by default, throw `UnsupportedOperationException`s if not overridden. You can register new inbound packets by adding a new
line in `PacketLoader`'s `loadAll` function, with something like `PacketState.STATE.registerPacketType(ID, ::PacketInSomething)`,
which will add the packet to the state's internal packet map, which is used by the Netty handlers to look up and find the
packet by the read ID.

The naming convention for packet classes is as follows: `Packet` + direction (`In` or `Out`) + name as documented on
[wiki.vg](https://wiki.vg/Protocol). The direction in this context is relative to the server, in that `In` means inbound
(to the server), and `Out` means outbound (from the server). For example, the clientbound statistics packet should be named
`PacketOutStatistics`.

## Formatting

Your commit messages for Git should preferably be short and concise, and describe the changes that you have made in it.
It is strongly recommended that you only make one change per commit, and this will help with keeping your description
short.

Krypton uses a Kotlin static analysis tool called "Detekt". This is already pre-configured to the specification of the
project, and should be closely adhered to. It may sometimes, however, be wrong in its findings, and in that case, issues
may be suppressed by using the module's `baseline.xml` file (found in the `config` folder in the module's folder). More
information on Detekt's baseline file can be found [here](https://detekt.github.io/detekt/baseline.html).

## Other

If you have any suggestions that you think would make this contribution guide better, perhaps either making this more
informative, removing informality, fixing grammar or typos, or anything else, feel free to either open an issue or make
a pull request.

And finally, thank you for spending the time to read this contribution guide! Happy contributing!
