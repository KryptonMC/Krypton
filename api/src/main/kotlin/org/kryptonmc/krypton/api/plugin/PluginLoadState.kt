package org.kryptonmc.krypton.api.plugin

/**
 * Represents the load state of a [Plugin]
 *
 * There are four load states: initialising, initialised, shutting down and shut down:
 * - In the initialising phase, the plugin's description file and main class will be loaded
 *   and the plugin will be called. This will also call the [Plugin.initialize] method.
 * - In the initialised phase, the plugin is ready to use.
 * - In the shutting down phase, the plugin will be fully unloaded.
 * - In the shut down phase, the plugin is unloaded and should not be used.
 */
enum class PluginLoadState {
    INITIALIZING,
    INITIALIZED,
    SHUTTING_DOWN,
    SHUT_DOWN
}
