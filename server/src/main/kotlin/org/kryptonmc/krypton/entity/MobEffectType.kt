package org.kryptonmc.krypton.entity

enum class MobEffectType(private val id: Int) {

    SPEED(1),
    SLOW(2),
    HASTE(3),
    MINING_FATIGUE(4),
    STRENGTH(5),
    HEALING(6),
    HARMING(7),
    JUMP_BOOST(8),
    NAUSEA(9),
    REGENERATION(10),
    RESISTANCE(11),
    FIRE_RESISTANCE(12),
    WATER_BREATHING(13),
    INVISIBILITY(14),
    BLINDNESS(15),
    NIGHT_VISION(16),
    HUNGER(17),
    WEAKNESS(18),
    POISON(19),
    WITHER(20),
    HEALTH_BOOST(21),
    ABSORPTION(22),
    SATURATION(23),
    GLOWING(24),
    LEVITATION(25),
    LUCK(26),
    UNLUCK(27),
    SLOW_FALLING(28),
    CONDUIT_POWER(29),
    DOLPHINS_GRACE(30),
    BAD_OMEN(31),
    HERO_OF_THE_VILLAGE(32);

    companion object {
        private val mobEffectIds = values().toList()
        private val mobEffectNames = mobEffectIds.associateBy { it.name }
        fun fromId(id: Int): MobEffectType? {
            if (id >= 0 && id < mobEffectIds.size) return mobEffectIds[id]
            return null
        }

        fun fromName(id: String) = mobEffectNames[id]
    }

    override fun toString() = "PotionEffectType[$id, $name]"
}
