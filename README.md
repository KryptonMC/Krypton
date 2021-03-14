[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.kryptonmc.org%2Fjob%2FKrypton)](https://ci.kryptonmc.org/job/Krypton)
[![Discord](https://img.shields.io/discord/815157416563834881?color=%237289da&label=discord)](https://discord.gg/https://discord.gg/4QuwYACDRX)

# Krypton

Krypton is a Minecraft server implementation, written purely from scratch in Kotlin.

It is currently a W.I.P, so if you find any bugs, feel free to make an issue about them

### Usage

We now have a Jenkins CI server! You can find it at https://ci.kryptonmc.org

You can download the latest JAR file from here and run it with `java -jar Krypton-VERSION.jar`. If you have any questions,
feel free to ask in our Discord server.

Or, if you prefer, you can clone the repository with `git clone https://github.com/KryptonMC/Krypton.git`
and build from source with `gradle shadowJar` if you wish to run the server.

**WARNING: Krypton is far from production ready. DO NOT USE ON PRODUCTION SERVERS! WILL CONTAIN BUGS! You have been warned.**

### API

As of version 0.11, Krypton now has an API. It is heavily in development, so expect to find bugs, but it is
at least semi-functional.

* You can depend on it like this:
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

* Or alternatively, with Maven:
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

Beware that none of the documentation for this is publicly hosted yet.
You can view all of the documentation by depending on the artifact and looking at the source files.

* Example usage:
```kotlin
class MyPlugin(context: PluginContext) : Plugin(context) {

    init {
        registerCommand(MyCommand()) // example command registration
    }

    // this is called separately after instantiation, on its own scope, so this is preferred over init where necessary
    override suspend fun initialize() {
        // do some other things
    }
}
```

If you have any questions about the usage of the API, feel free to join our Discord server!
If you have an idea on how I could improve the API, feel free to open an issue.

### Contributing

Krypton is open-source for a reason. You are more than welcome to contribute, and we even encourage it. This project wouldn't
be possible without contributions.