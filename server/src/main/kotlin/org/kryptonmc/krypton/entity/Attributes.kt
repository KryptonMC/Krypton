package org.kryptonmc.krypton.entity

import net.kyori.adventure.util.Index
import org.kryptonmc.krypton.api.registry.NamespacedKey
import java.util.*

data class Attribute(
    val key: AttributeKey,
    val value: Double = key.default,
    val modifiers: List<AttributeModifier> = emptyList()
)

enum class AttributeKey(val key: NamespacedKey, val default: Double, val min: Double, val max: Double, val label: String = "") {

    GENERIC_MAX_HEALTH(NamespacedKey(value = "generic.max_health"), 20.0, 0.0, 1024.0, "Max Health."),
    GENERIC_FOLLOW_RANGE(NamespacedKey(value = "generic.follow_range"), 32.0, 0.0, 2048.0, "Follow Range."),
    GENERIC_KNOCKBACK_RESISTANCE(NamespacedKey(value = "generic.knockback_resistance"), 0.0, 0.0, 1.0, "Knockback Resistance."),
    GENERIC_MOVEMENT_SPEED(NamespacedKey(value = "generic.movement_speed"), 0.7, 0.0, 1024.0, "Movement Speed."),
    GENERIC_ATTACK_DAMAGE(NamespacedKey(value = "generic.attack_damage"), 2.0, 0.0, 2048.0, "Attack Damage."),
    GENERIC_ATTACK_SPEED(NamespacedKey(value = "generic.attack_speed"), 4.0, 0.0, 1024.0, "Attack Speed."),
    GENERIC_FLYING_SPEED(NamespacedKey(value = "generic.flying_speed"), 0.4, 0.0, 1024.0, "Flying Speed."),
    GENERIC_ARMOR(NamespacedKey(value = "generic.armor"), 0.0, 0.0, 30.0, "Armor."),
    GENERIC_ARMOR_TOUGHNESS(NamespacedKey(value = "generic.armor_toughness"), 0.0, 0.0, 20.0, "Armor Toughness"),
    GENERIC_ATTACK_KNOCKBACK(NamespacedKey(value = "generic.attack_knockback"), 0.0, 0.0, 5.0),
    GENERIC_LUCK(NamespacedKey(value = "generic.luck"), 0.0, -1024.0, 1024.0, "Luck."),
    HORSE_JUMP_STRENGTH(NamespacedKey(value = "horse.jump_strength"), 0.7, 0.0, 2.0, "Jump Strength."),
    ZOMBIE_SPAWN_REINFORCEMENTS(NamespacedKey(value = "zombie.spawn_reinforcements"), 0.0, 0.0, 1.0, "Spawn Reinforcements Chance.");

    companion object {

        private val BY_KEY = Index.create(AttributeKey::class.java) { it.key }

        fun fromKey(key: NamespacedKey) = requireNotNull(BY_KEY.value(key))
    }
}

data class AttributeModifier(
    val name: String,
    val uuid: UUID,
    val amount: Double,
    val operation: ModifierOperation
)

enum class ModifierOperation(val id: Int) {

    ADD(0),
    MULTIPLY_BASE(1),
    MULTIPLY(2)
}