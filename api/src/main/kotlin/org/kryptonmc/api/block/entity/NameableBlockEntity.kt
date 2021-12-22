package org.kryptonmc.api.block.entity

import net.kyori.adventure.text.Component
import org.kryptonmc.api.util.TranslationHolder

/**
 * A block entity that has an associated custom name.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface NameableBlockEntity : BlockEntity, TranslationHolder {

    /**
     * The display name of this block entity.
     */
    @get:JvmName("displayName")
    public var displayName: Component
}
