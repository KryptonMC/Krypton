package org.kryptonmc.krypton.item.meta

import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Test
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ImmutableCompoundTag

class ItemMetaTests {

    @Test
    fun `verify equals and hash code`() {
        EqualsVerifier.forClass(AbstractItemMeta::class.java)
            .usingGetClass()
            .withOnlyTheseFields("data")
            .withPrefabValues(
                CompoundTag::class.java,
                ImmutableCompoundTag.builder().putInt("damage", 3).build(),
                ImmutableCompoundTag.builder().putInt("CustomModelData", 254).build()
            )
            .verify()
    }
}
