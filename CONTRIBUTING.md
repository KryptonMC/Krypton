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
or you can just browse some files and have a look for yourself).
We closely follow the official [Kotlin conventions](https://kotlinlang.org/docs/coding-conventions.html), with the following constraints:
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
beginners to pick up, but also be advanced enough that more experienced developers can do a lot with it. We mainly take
inspiration from Bukkit, Sponge, Velocity, and Minestom when choosing how to design new APIs.

Plugins should be heavily reliant on dependency injection, and using static accessors is not recommended, with the exception
of catalogue classes and registries. We use [Guice](https://github.com/google/guice) for dependency injection, which is
quite easy to pick up, if you can make sense of dependency inversion.

Use of classes should be avoided, in favour of interfaces with factories, in order to abstract away as much implementation
detail as possible, help with maintaining and updating the API for future versions of Minecraft, and also in some cases
permit custom implementations of parts of the API.

Also, Krypton's API should be designed in a way that allows it to evolve nicely with new Minecraft versions. That isn't to
say that new API changes are not welcome, but please consider the impact that your changes may have in the future.
You should also look in to potential use cases as well as potential maintenance costs. If the negatives outweigh the positives,
it's probably not worth it.

In Krypton's current state, maintaining backwards compatibility is not a priority, and things like deprecation and removal processes
aren't something that we are doing yet. These are, however, on the list of things that will likely be implemented with a stable
release of the API.

### Server

The server is a bit more complex than the API. It does not have many of the strict requirements of the API, like Java
compatibility or abstraction layers for plugins, but it still does have some guidelines that you should at least attempt
to follow.

The server is not designed in a way that will maintain backwards compatibility for dependents of it, as this would have too high
of a maintenance cost, and severely limit our ability to evolve Krypton through Minecraft versions.
Generally, there will be certain things within the server that are provided purely for the purpose of being used as an API, and
these will usually be considered somewhat public API, though they can be changed if it becomes necessary to do so.

Also, when designing implementations, you should be careful to ensure that you are:
* Properly following the specification for the API you are implementing
* Not introducing anything that may be liable to issues, such as throwing exceptions where they weren't expected
* Testing your code quite thoroughly to ensure that it works the way you intend it to

## Formatting

Your commit messages for Git should preferably be short and concise, and describe the changes that you have made in it.
It is strongly recommended that you only make one change per commit, and this will help with keeping your description
short.

Krypton uses a Kotlin static analysis tool called Detekt. This is already pre-configured for the API, and it is mandatory that
this be adhered to, else building the project will fail. The rules are rather lenient, and so you should have no issues if you
are following proper procedures, though you can add exclusions in Detekt's `baseline.xml`. More information about how to setup
the baseline can be found [here](https://detekt.github.io/detekt/baseline.html).

## Other

If you have any suggestions that you think would make this contribution guide better, perhaps either making this more
informative, removing informality, fixing grammar or typos, or anything else, feel free to either open an issue or make
a pull request.

And finally, thank you for spending the time to read this contribution guide! Happy contributing!
