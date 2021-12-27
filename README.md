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

Whilst Krypton is tested, and most features should work as intended, it is in no way as stable as other server software, such as Paper,
which is much more thoroughly tested. Also, Krypton is still in development, so bugs are still likely to occur in places where we haven't
looked.
**Krypton is not liable for any damage caused by its usage in production environments.**

### API

For information on how to use the API, see the official wiki [here](https://wiki.kryptonmc.org), and the KDocs (Kotlin equivalent of
JavaDocs) [here](https://docs.kryptonmc.org).

### Contributing

Krypton is open-source for a reason. You are more than welcome to contribute, and we even encourage it.

If you're interested, I highly recommend coming and talking to us on our official Discord server. This is where you can talk to the
development team, who know the most about the project, and they can offer you advice and information on where to start.
You should also read the [contribution guide](CONTRIBUTING.md) for some common information, though this is incomplete, and needs more
information in it.

### Credits

- The project's [contributors](https://github.com/KryptonMC/Krypton/graphs/contributors) (of course)
- [The Velocity project](https://velocitypowered.com/), for providing the fast networking and amazing plugin loading and event systems that
  the Krypton API contains derivatives of.
- [Minestom](https://minestom.net), for their support, API, and amazing community of welcoming individuals that have helped this project
  since it's very beginning.
- [The Minecraft Coalition](https://wiki.vg) for their hard work and effort documenting the protocol, allowing these projects to exist,
  and their amazing support.
- [The Minecraft Wiki](https://minecraft.gamepedia.com), for their amazing efforts documenting just about everything
  there is to know about Minecraft, and making it available for everyone to use.
- The project's dependencies, each and every one helping to make our lives as developers easier.
- [JProfiler](https://www.ej-technologies.com/products/jprofiler/overview.html), for being kind enough to grant us an
  open-source license for their profiler, helping to ensure that Krypton can perform well in production environments.
