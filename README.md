[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.kryptonmc.org%2Fjob%2FKrypton)](https://ci.kryptonmc.org/job/Krypton)
[![Discord](https://img.shields.io/discord/815157416563834881?color=%237289da&label=discord)](https://discord.gg/https://discord.gg/4QuwYACDRX)

# Krypton

Krypton is a Minecraft server implementation, written purely from scratch in Kotlin.

It is currently a W.I.P, so if you find any bugs, feel free to make an issue about them

### Usage

We now have a Jenkins CI server! You can find it at https://ci.kryptonmc.org

You can download the latest JAR file from here and run it with `java -jar Krypton-VERSION.jar`. If you have any
questions, feel free to ask in our Discord server.

Or, if you prefer, you can clone the repository with `git clone https://github.com/KryptonMC/Krypton.git`
and build from source with `gradle shadowJar` if you wish to run the server.

**WARNING: Krypton is far from production ready. DO NOT USE ON PRODUCTION SERVERS! WILL CONTAIN BUGS! You have been
warned.**

### API

As of version 0.11, Krypton now has an API. It is heavily in development, so expect to find bugs, but it is at least
semi-functional.

You can depend on it like this:

* Gradle Kotlin DSL:

```kotlin
repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
    maven("https://repo.bristermitten.me/repository/maven-public/")
}

dependencies {
    compileOnly("org.kryptonmc:krypton-api:LATEST")
}
```

* Gradle Groovy DSL:

```groovy
repositories {
    mavenCentral()
    maven { url 'https://libraries.minecraft.net' } // for Brigadier
    maven { url 'https://repo.bristermitten.me/repository/maven-public/' }
}

dependencies {
    compileOnly 'org.kryptonmc:krypton-api:LATEST'
}
```

* Maven:

```xml
<repositories>
    <!-- For Brigadier -->
    <repository>
        <id>minecraft</id>
        <url>https://libraries.minecraft.net</url>
    </repository>
    <repository>
        <id>bristermitten</id>
        <url>https://repo.bristermitten.me/repository/maven-public/</url>
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

The documentation for both the API and the server can now be found [here](https://docs.kryptonmc.org), and the
wiki on how to use the API can be found [here](https://wiki.kryptonmc.org)

Example usage:

```kotlin
// As of 0.22.1, we now use Guice, meaning you can now inject your required dependencies
// in any one of your classes throughout the project
class MyPlugin @Inject constructor(commandManager: CommandManager, command: MyCommand) {

    init {
        commandManager.register(command) // example command registration
    }

    @Listener
    fun initialize() {
        // This is called after things like your plugin's container have finished initialising,
        // so it is safe to use them here.
    }
}
```

If you have any questions about the usage of the API, feel free to join our Discord server!

Or if you have an idea on how I could improve the API, feel free to open an issue.

### Contributing

Krypton is open-source for a reason. You are more than welcome to contribute, and we even encourage it. This project
wouldn't be possible without contributions.

If you're interested, I highly recommend reading the [contribution guide](CONTRIBUTING.md) to learn how to get started,
and know the best practices.

### Credits

- The project's [contributors](https://github.com/KryptonMC/Krypton/graphs/contributors) (of course)
- [The Velocity project](https://velocitypowered.com/), for providing the fast networking and amazing plugin loading and
  event systems.
- [The Sponge Project](https://spongepowered.org), for providing the registry system that ours is partially derived
  from.
- [The Minecraft Coalition](https://wiki.vg) and [`#mcdevs`](https://github.com/mcdevs), for their hard work and effort
  documenting the protocol, allowing these projects to exist, and their amazing support.
- [The Minecraft Wiki](https://minecraft.gamepedia.com), for their amazing efforts documenting just about everything
  there is to know about Minecraft, and making it available for everyone to use.
- The project's dependencies, each and every one helping to make our lives as developers easier. Notable
  mentions: [Adventure](https://github.com/KyoriPowered/Adventure), [Kotlin](https://kotlinlang.org),
  [Netty](https://netty.io), [OkHttp](https://square.github.io/okhttp/), [Log4J](https://logging.apache.org/log4j/2.x/)
- [JProfiler](https://www.ej-technologies.com/products/jprofiler/overview.html), for being kind enough to grant us an
  open-source license for their profiler.
