package me.bristermitten.minekraft.entities.block

enum class BlockActionUpdate(val id: Int) {

    /**
     * Set data of a mob spawner (everything except for SpawnPotentials: current delay, min/max delay,
     * mob to be spawned, spawn count, spawn range, etc.)
     */
    SET_MOB_SPAWNER_DATA(1),

    /**
     * Set command block text (command and last execution status)
     */
    SET_COMMAND_BLOCK_DATA(2),

    /**
     * Set the level, primary, and secondary powers of a beacon
     */
    SET_BEACON_DATA(3),

    /**
     * Set rotation and skin of mob head
     */
    SET_MOB_HEAD_DATA(4),

    /**
     * Declare a conduit
     */
    DECLARE_CONDUIT(5),

    /**
     * Set base colour and patterns on a banner
     */
    SET_BANNER_DATA(6),

    /**
     * Set the data for a Structure tile entity
     */
    SET_STRUCTURE_DATA(7),

    /**
     * Set the destination for an end gateway
     */
    SET_END_GATEWAY_DESTINATION(8),

    /**
     * Set the text on a sign
     */
    SET_SIGN_TEXT(9),

    /**
     * Declare a bed
     */
    DECLARE_BED(11),

    /**
     * Set data of a jigsaw block
     */
    SET_JIGSAW_DATA(12),

    /**
     * Set items in a campfire
     */
    SET_CAMPFIRE_DATA(13),

    /**
     * Beehive information
     */
    BEHIVE_INFORMATION(14),

    /**
     * Unused
     */
    UNUSED(10)
}