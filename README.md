[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/github/actions/workflow/status/KryptonMC/Krypton/build.yml)](https://github.com/KryptonMC/Krypton/actions/workflows/build.yml)
[![Latest API Version](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.kryptonmc.org%2Freleases%2Forg%2Fkryptonmc%2Fkrypton-api%2Fmaven-metadata.xml)](https://repo.kryptonmc.org/#/releases/org/kryptonmc/krypton-api)
[![Discord](https://img.shields.io/discord/815157416563834881?color=%237289da&label=discord)](https://discord.gg/4QuwYACDRX)

# Krypton

Krypton is free and open-source Minecraft server software, written from scratch, without Mojang code.

It is currently a work in progress, so if you find any bugs, feel free to either make a GitHub issue about them,
or join our Discord and ask in one of the help channels.

Please join our Discord to keep up with the latest changes and chat with the community!
![Krypton Discord](https://discordapp.com/api/guilds/815157416563834881/widget.png?style=banner3)

### Usage

We have a Jenkins CI server, where the project is built for distribution. You can find it at https://ci.kryptonmc.org

You can download the latest JAR file from here and run it with `java -jar Krypton-VERSION.jar`. If you have any questions or issues running the JAR,
feel free to ask in our Discord server.

Or, if you prefer, you can clone the repository with `git clone https://github.com/KryptonMC/Krypton.git`
and build from source with `gradle shadowJar` if you wish to run the server.

**Note**: As it stands, Krypton does not have any world generation. Therefore, you must provide your own world generated
from vanilla Minecraft, and set the world name in the `config.conf` to the **name** of the **folder** the world is in, else
Krypton will fail to start. This will not always be the case, however, world generation is a large task that I do not want
to even think about getting started on yet.

The testing in Krypton is, as it stands, not satisfactory, and things may break at any time, often without clear reason.
However, we are working towards better testing, and hopefully, this will change in the future.

In addition, Krypton is still in development, and the community testing is nowhere near as vast as other software.

### API

For information on how to use the API, see the official wiki [here](https://wiki.kryptonmc.org), and the
KDocs (Kotlin equivalent of JavaDocs) [here](https://docs.kryptonmc.org).

### Contributing

Krypton is open-source for a reason. You are more than welcome to contribute, and we even encourage it.

If you're interested, I highly recommend coming and talking to us on our official Discord server. This is where you can talk to the
development team, who know the most about the project, and they can offer you advice and information on where to start.
You should also read the [contribution guide](CONTRIBUTING.md) for some common information, though this is incomplete, and needs more
information in it.

### Comparison with other software
Here, I've tried to compare Krypton with other, existing solutions. I don't expect
anyone to get convinced to use Krypton after reading this section, but hey, I'm not
going to shield you from the truth.

#### Paper
Paper is a highly optimised, very well known, very widely used, mature, and
battle-tested platform and fork of Spigot, itself a fork of the original
CraftBukkit.

It is developed by a highly skilled and very knowledgeable team of individuals, who
know an awful lot more than I do about this. Paper is much better for use in
production envionments, on vanilla servers, and many other tasks.

#### Sponge
Sponge is a very well known, widely used, and mature platform, mostly aimed at
providing an alternative API to Bukkit, and supporting mod/plugin hybrid
environments.

It is also developed by a highly skilled and very knowledgeable team of individuals,
who also know an awful lot more than I do about this. Sponge is also much better
for use in production environments, on vanilla servers, and also hybrid servers, as
well as other tasks.

#### Minestom
This is where a lot of our community are from, so it's only fair I offer a comparison
with this. Minestom is a less well known, much less widely used, and much less mature
platform, with a highly dedicated, skilled, and knowledgeable team of individuals,
offering Minecraft servers a better way to create games that do not require vanilla
features.

Minestom is much more suitable for games servers, where it would take more time to
remove all the vanilla features you do not require than add all the features you do
require.

#### Conclusion
For high performance vanilla, use Paper. For mod/plugin hybrids and a better API use
Sponge. For high performance minigames and other games, use Minestom.

If you're still reading after all this, Krypton is highly experimental, not battle
tested, not mature, not stable, and not production ready. It has hardly any vanilla
features, and the performance is worse than vanilla, even with the lack of features.

However, it offers a new and modern API, not limited by backwards compatibility
requirements, or Mojang's server design, and aims to provide a new, and hopefully
better, experience for developers. The project still has potential to grow, and is
very open to contribution, and is really striving to have the voices of its community
heard.

### Credits

- The project's [contributors](https://github.com/KryptonMC/Krypton/graphs/contributors), of course, for their amazing work
helping to make this possible.
- [The Velocity project](https://velocitypowered.com/), for providing the fast networking and amazing plugin loading and event systems that the Krypton API contains derivatives of.
- [Minestom](https://minestom.net), for their support, API, and amazing community of welcoming individuals that have helped this project since it's very beginning.
- [The Minecraft Coalition](https://wiki.vg), for their hard work and effort documenting the protocol, allowing these projects to exist.
- [The Minecraft Wiki](https://minecraft.gamepedia.com), for their amazing efforts documenting just about everything
  there is to know about Minecraft, and making it available for everyone to use.
- The project's dependencies, each and every one helping to make our lives as developers easier.
- [JProfiler](https://www.ej-technologies.com/products/jprofiler/overview.html), for being kind enough to grant us an
  open-source license for their profiler, helping to ensure that Krypton can perform well in production environments.
- [Arcmutate](https://www.arcmutate.com/), for being kind enough to grant us an open-source license for their Kotlin extensions to the PIT mutation
  testing software that we use to ensure that test coverage is at an acceptable level, helping to ensure the stability of Krypton.
