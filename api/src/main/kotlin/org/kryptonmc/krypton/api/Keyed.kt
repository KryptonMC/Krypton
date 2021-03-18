package org.kryptonmc.krypton.api

import org.kryptonmc.krypton.api.registry.NamespacedKey

/**
 * Represents an object that has a [NamespacedKey] attached to it.
 *
 * @author Callum Seabrook
 */
interface Keyed {

    /**
     * The key
     */
    val key: NamespacedKey
}