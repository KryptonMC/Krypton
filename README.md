[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/github/actions/workflow/status/KryptonMC/Krypton/build.yml)](https://github.com/KryptonMC/Krypton/actions/workflows/build.yml)
[![Latest API Version](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.kryptonmc.org%2Freleases%2Forg%2Fkryptonmc%2Fkrypton-api%2Fmaven-metadata.xml)](https://repo.kryptonmc.org/#/releases/org/kryptonmc/krypton-api)
[![Discord](https://img.shields.io/discord/815157416563834881?color=%237289da&label=discord)](https://discord.gg/4QuwYACDRX)

# Krypton

Krypton is free and open-source Minecraft server software, written from scratch, without Mojang code.

It is currently a work in progress, so if you find any bugs, feel free to either make a GitHub issue about them,
or join our Discord and ask in one of the help channels.

Please join our Discord to keep up with the latest changes and chat with the community!
[![Krypton Discord](https://discordapp.com/api/guilds/815157416563834881/widget.png?style=banner3)](https://discord.gg/4QuwYACDRX)

### Usage

We use GitHub Actions to build the project, and the results are deployed to our downloads API. There will soon be a downloads page to download
the project from, but this does not yet exist.

To download the latest JAR, you can go to https://api.kryptonmc.org/downloads/v1/krypton/latest/download

Starting the server is as easy as running the JAR, which can be done with `java -jar krypton-VERSION.jar`.
If you have any questions or issues running the JAR, feel free to ask in our Discord server.

Or, if you prefer, you can clone the repository with `git clone https://github.com/KryptonMC/Krypton.git`
and build from source with `gradle shadowJar` if you wish to run the server. The full artifact will appear in
`jar/build/libs/Krypton-VERSION.jar`.

**Note**: As it stands, Krypton does not have any world generation. Therefore, you must provide your own world generated
from vanilla Minecraft, and set the world name in the `config.conf` to the **name** of the **folder** the world is in, else
Krypton will fail to start. This will not always be the case, however, world generation is a large task that I do not want
to even think about getting started on yet.

Krypton is properly tested manually when changes are made, and we do have some level of automated testing, however it is not
as extensively tested as Paper or other server software, or to the same standard as professional software, as this is not
professional software.

### API

For information on how to use the API, see the official wiki [here](https://wiki.kryptonmc.org), and the
KDocs (Kotlin equivalent of JavaDocs) [here](https://docs.kryptonmc.org).

### Contributing

Krypton is open-source for a reason. Anyone is welcome to contribute, but it may be difficult knowing where to start or being
able to understand the codebase, as it is rather large, and very under documented. If you do struggle, reach out on Discord and
one of the core team can help (most likely me, BomBardyGamer).

It is also highly recommend to read the [contribution guide](CONTRIBUTING.md) before contributing to ensure that you stick to
the general style guidelines and recommendations of the project.

### Comparison with other software

#### Paper

Paper is a highly optimised, very well known, very widely used, mature, and
battle-tested platform and fork of Spigot, itself a fork of the original
CraftBukkit.

It is developed by a highly skilled and very knowledgeable team of individuals.
Paper is much better for use in production envionments, on vanilla servers, and many other tasks.

However, as Paper is based on vanilla Minecraft, it still has some issues. Mainly, RAM usage on even
small Minecraft servers on new versions is very high.

In addition, Paper is based on Bukkit, and the Bukkit API is very old, and has a lot of issues
with lack of support for certain features, and a core design that does not accurately represent modern
Minecraft versions.

#### Sponge

Sponge is a very well known, widely used, and mature platform, mostly used within the
modding community as a way to run plugins alongside mods.

It is also developed by a highly skilled and very knowledgeable team of individuals.
Sponge is also much better for use in production environments, on vanilla servers, and also
hybrid servers, as well as other tasks.

However, like Paper, it is based on vanilla Minecraft, but it is not as well optimised as Paper,
as optimisation is not Sponge's focus, and so the RAM usage is also not that good.

#### Minestom

This is where a lot of our community are from, so it's only fair I offer a comparison
with this. Minestom is a less well known, much less widely used, and much less mature
platform, with a highly dedicated, skilled, and knowledgeable team of individuals,
offering Minecraft servers a better way to create games that do not require vanilla
features.

Some significant parts of Krypton's API and backend are based on Minestom code, like scheduling, events,
and networking, to name a few.

Minestom is much more suitable for games servers, where it would take more time to
remove all the vanilla features you do not require than add all the features you do
require.

#### Conclusion

Krypton is experimental software. It is not battle-tested. It does not offer the incredible performance
of Minestom. It does not yet have all vanilla features implemented. It is recommended for those curious
about vanilla, and who are interested in an alternative, that aims to one day be mostly compatible.

Current stress tests show that the server can handle at least 500 concurrent bots with no delays, issues with
joining or pinging, or any other network issues. I have had it run smoothly with 1000 bots before, but this will
have to be tested again. At 2000 players, it started running out of memory to allocate buffers, at which point
everything started to grind to a halt, and the server process had to be force closed. I am looking to improve
networking performance, and hopefully reach 2000 players, but we are not there yet.

In addition, Krypton's API is designed from scratch. It takes inspiration mainly from Sponge and Minestom, with
parts from Velocity, such as plugin loading, and possibly some parts from Bukkit. It is designed to be simple
enough to use without reading loads of documentation, but also powerful enough to manipulate a lot of vanilla
features and mechanics, and build plugins that can change the way parts of the game work.

### Credits

- The project's [contributors](https://github.com/KryptonMC/Krypton/graphs/contributors), of course, for their amazing work
helping to make this possible.
- [The Velocity project](https://velocitypowered.com/), for providing the plugin loading and permission systems that the Krypton API contains derivatives of.
- [Minestom](https://minestom.net), for their support, API, and amazing community of welcoming individuals that have helped this project since it's very beginning.
- [The Minecraft Coalition](https://wiki.vg), for their hard work and effort documenting the protocol, allowing these projects to exist.
- [The Minecraft Wiki](https://minecraft.gamepedia.com), for their amazing efforts documenting just about everything
  there is to know about Minecraft, and making it available for everyone to use.
- The project's dependencies, each and every one helping to make our lives as developers easier.

### Special Thanks

[![JProfiler](https://www.ej-technologies.com/images/product_banners/jprofiler_large.png)](https://www.ej-technologies.com/products/jprofiler/overview.html)

We would also like to say a special thank you to the team at EJ technologies, for being kind enough to
provide us with an open-source license for [JProfiler](https://www.ej-technologies.com/products/jprofiler/overview.html),
which allows me to profile the software and continue to make improvements to the performance.
