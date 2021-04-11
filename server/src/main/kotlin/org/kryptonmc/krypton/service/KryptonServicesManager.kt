package org.kryptonmc.krypton.service

import org.kryptonmc.krypton.api.plugin.Plugin
import org.kryptonmc.krypton.api.service.ServiceProvider
import org.kryptonmc.krypton.api.service.ServicesManager
import java.util.concurrent.*

class KryptonServicesManager : ServicesManager {

    private val providers = ConcurrentHashMap<Class<*>, MutableList<KryptonServiceProvider<*>>>()

    override fun <T> register(plugin: Plugin, clazz: Class<T>, service: T) = synchronized(providers) {
        providers.getOrPut(clazz) { mutableListOf() } += KryptonServiceProvider(plugin, clazz, service)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getProvider(clazz: Class<T>): ServiceProvider<T>? = synchronized(providers) {
        providers[clazz] as? ServiceProvider<T>
    }
}