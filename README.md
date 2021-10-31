[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.kryptonmc.org%2Fjob%2FKrypton)](https://ci.kryptonmc.org/job/Krypton)
[![Discord](https://img.shields.io/discord/815157416563834881?color=%237289da&label=discord)](https://discord.gg/https://discord.gg/4QuwYACDRX)

# Krypton

Krypton is free and open-source Minecraft server software, a bit like Paper or Sponge (if you've heard of those), but it's different, in that it
isn't based on any Mojang code. Krypton is written entirely from the ground up.

It is currently a W.I.P, so if you find any bugs, feel free to make an issue about them.

### Usage

We have a Jenkins CI server, where the project is built for distribution. You can find it at https://ci.kryptonmc.org

You can download the latest JAR file from here and run it with `java -jar Krypton-VERSION.jar`. If you have any questions or issues running the JAR,
feel free to ask in our Discord server.

Or, if you prefer, you can clone the repository with `git clone https://github.com/KryptonMC/Krypton.git`
and build from source with `gradle shadowJar` if you wish to run the server.

**WARNING: Krypton is far from production ready. DO NOT USE ON PRODUCTION SERVERS! WILL CONTAIN BUGS! You have been
warned.**

### API

As of version 0.11, Krypton has an API. It is heavily in development, so expect to find bugs, but it is constantly evolving, with new features
being added all the time.

You can depend on it like this:

* Gradle Kotlin DSL:

```kotlin
repositories {
    maven("https://repo.kryptonmc.org/releases")
}

dependencies {
    compileOnly("org.kryptonmc:krypton-api:LATEST")
}
```

* Gradle Groovy DSL:

```groovy
repositories {
    maven { url 'https://repo.kryptonmc.org/releases' }
}

dependencies {
    compileOnly 'org.kryptonmc:krypton-api:LATEST'
}
```

* Maven:

```xml
<repositories>
    <repository>
        <id>kryptonmc</id>
        <url>https://repo.kryptonmc.org/releases</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.kryptonmc</groupId>
        <artifactId>krypton-api</artifactId>
        <version>LATEST</version>
    </dependency>
</dependencies>
```

The documentation for both the API and the server can now be found [here](https://docs.kryptonmc.org), and the wiki on how to use the API
can be found [here](https://wiki.kryptonmc.org).

Example usage:

```kotlin
@Plugin(id = "my-plugin")
class MyPlugin @Inject constructor(commandManager: CommandManager) {

    init {
        commandManager.register(MyCommand()) // example command registration
    }

    @Listener
    fun onStart(event: ServerStartEvent) {
        // This is called after things like your plugin's container have finished initialising,
        // so it is safe to use them here.
    }
}
```

If you have any questions about API usage, or any feedback about how the API could be improved, feel free to join the Discord server.

### Contributing

Krypton is open-source for a reason. You are more than welcome to contribute, and we even encourage it. This project
wouldn't be possible without contributions.

If you're interested, I highly recommend reading the [contribution guide](CONTRIBUTING.md) to learn how to get started,
and know the best practices, and to talk to us on our official Discord server for help.

### Credits

- The project's [contributors](https://github.com/KryptonMC/Krypton/graphs/contributors) (of course)
- [The Velocity project](https://velocitypowered.com/), for providing the fast networking and amazing plugin loading and event systems that
  the Krypton API contains derivatives of.
- [The Sponge Project](https://spongepowered.org), for their amazing work creating an API that far exceeds anything that has been achieved
  by its rivals, and for inspiring a significant amount of the design of the Krypton API.
- [The Minecraft Coalition](https://wiki.vg) for their hard work and effort documenting the protocol, allowing these projects to exist,
  and their amazing support.
- [The Minecraft Wiki](https://minecraft.gamepedia.com), for their amazing efforts documenting just about everything
  there is to know about Minecraft, and making it available for everyone to use.
- The project's dependencies, each and every one helping to make our lives as developers easier. Notable mentions: [Kotlin](https://kotlinlang.org),
  [Adventure](https://github.com/KyoriPowered/Adventure), [Configurate](https://github.com/SpongePowered/Configurate), [Netty](https://netty.io),
  [Log4J](https://logging.apache.org/log4j/2.x/).
- [JProfiler](https://www.ej-technologies.com/products/jprofiler/overview.html), for being kind enough to grant us an
  open-source license for their profiler.
