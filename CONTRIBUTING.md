# Krypton contribution guide

Hey there! Good to see you've decided to open this document! I hope you enjoy your stay, and I hope this proves useful to you.

First of all, thank you for considering contributing to Krypton! I really appreciate all who do! This project may not survive without contributions.

**Table of contents:**
* [Requirements](#requirements)
* [Specific Information](#specific-information)
    * [API](#api)
    * [Server](#server)
        * [Packets](#packets)
* [Formatting](#formatting)
* [Other](#other)

## Requirements

First of all, this guide assumes that you have some knowledge on how to program in Kotlin, and you have chosen your IDE to use.
If you don't know how to program in Kotlin, I recommend the [official Kotlin documentation](https://kotlinlang.org/docs/home.html).

Secondly, you'll need a few tools to start making contributions. Most of these should come with your IDE, but some may not:
* Gradle
* Git
* Some form of Kotlin support / Kotlin plugin

If your IDE doesn't have support for Gradle, you can use the Gradle wrapper (`gradlew` or `gradlew.bat`), which requires no installation and no setup.
For Git, you can download a standalone version [here](https://git-scm.com/downloads), though if you are using a Linux distribution, you probably already have Git.

Third and final, I recommend familiarising yourself with the code style of the project.
I recommend you browse some files in the project and learn how the project is styled.
You should also read the [style guide](STYLE_GUIDE.md) for more in-depth details.

## Specific Information
### API

The API is designed around that of it's predecessors. It is primarily designed to be concise enough to not be complex for beginners to pick up, but also be advanced enough that more experienced developers can do a lot with it. We mainly take inspiration from Bukkit, Sponge, Velocity, and Minestom when choosing how to design new APIs.

Plugins should be heavily reliant on dependency injection, and using static accessors is not recommended, with the exception of catalogue classes and registries. We use [Guice](https://github.com/google/guice) for dependency injection, which is quite easy to pick up, if you can make sense of dependency inversion.

Also, Krypton's API should be designed in a way that allows it to evolve nicely with new Minecraft versions.
That isn't to say that new API changes are not welcome, but please consider the impact that your changes may have in the future.
You should also look in to potential use cases as well as potential maintenance costs. If the negatives outweigh the positives, it's probably not worth it.

In Krypton's current state, maintaining backwards compatibility is not a priority, and things like deprecation and removal processes aren't something that we are doing yet.
These are, however, on the list of things that will likely be implemented with a stable release of the API.

### Server

The server is a bit more complex than the API. It does not have many of the strict requirements of the API, like Java compatibility or abstraction layers for plugins, but it still does have some guidelines that you should at least attempt to follow.

The server is not designed in a way that will maintain backwards compatibility for dependents of it, as this would have too high of a maintenance cost, and severely limit our ability to evolve Krypton through Minecraft versions.
Generally, there will be certain things within the server that are provided purely for the purpose of being used as an API, and these will usually be considered somewhat public API, though they can be changed if it becomes necessary to do so.

Also, when designing implementations, you should be careful to ensure that you are:
* Properly following the specification for the API you are implementing
* Not introducing anything that may be liable to issues, such as throwing exceptions where they weren't expected
* Testing your code quite thoroughly to ensure that it works the way you intend it to

## Documentation

The rules on documentation vary depending on what you're working on.

For the API, everything has to be documented, regardless of what it is. This is to avoid the possibility of something not getting documented, and so being ambiguous for people trying to understand how it works.
In addition, documentation should be clear and concise. You don't need to ramble on about every single detail about how something works, but don't be over ambiguous.
Also, word choice is key. For functions like `contains`, the word "checks" is often used. For
getters, consider "Gets". For functions that should create a new object, consider using the words "Creates a new {type}", where {type} is be replaced with the name of the thing you are creating, such as "Creates a new game profile".

For the server, not everything has to be documented, though everything that you may deem ambiguous should be documented, to reduce the time that people have to spend trying to decipher your code.

## Formatting

Your commit messages for Git should preferably be short and concise, and describe the changes that you have made in it.
It is strongly recommended that you only make one change per commit, and this will help with keeping your description
short.

Krypton uses a Kotlin static analysis tool called Detekt. This is already pre-configured for the API, and it is mandatory that this be adhered to, else building the project will fail. The rules are rather lenient, and so you should have no issues if you are following proper procedures, though you can add exclusions in Detekt's `baseline.xml`.
More information about how to setup the baseline can be found [here](https://detekt.github.io/detekt/baseline.html).

## Other

If you have any suggestions that you think would make this contribution guide better, perhaps either making this more informative, removing informality, fixing grammar or typos, or anything else, feel free to either open an issue or make a pull request.

And finally, thank you for spending the time to read this contribution guide! Happy contributing!
