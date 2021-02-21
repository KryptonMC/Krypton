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
    POSION(19),
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

        private val MOB_EFFECT_IDS = values()
        private val MOB_EFFECT_NAMES: Map<String, MobEffectType> = mutableMapOf()

        init {
            for (id in MOB_EFFECT_IDS) {
                MOB_EFFECT_NAMES.plus(id.name to id);
            }
        }

        @JvmStatic
        fun getFromId(id: Int): MobEffectType? {
            if (id >= 0 && id < MOB_EFFECT_IDS.size) {
                return MOB_EFFECT_IDS[id]
            }
            return null
        }

        @JvmStatic
        fun getFromName(id: String): MobEffectType? {
            return MOB_EFFECT_NAMES[id];
        }

    }

    override fun toString(): String {
        return "PotionEffectType[$id, $name]";
    }

}