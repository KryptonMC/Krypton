package org.kryptonmc.krypton.service

import org.kryptonmc.krypton.api.plugin.Plugin
import org.kryptonmc.krypton.api.service.ServiceProvider

data class KryptonServiceProvider<T>(
    override val plugin: Plugin,
    override val serviceClass: Class<T>,
    override val service: T
) : ServiceProvider<T>