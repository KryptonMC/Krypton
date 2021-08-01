/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.command

/**
 * The [permission level](https://minecraft.fandom.com/wiki/Permission_level) is primarily used when a player gets the operator status
 *
 * The permission levels get their permissions from the previous levels. So [PermissionLevel.LEVEL_2] gets all permissions from [PermissionLevel.LEVEL_1] plus their own
 *
 * And [PermissionLevel.LEVEL_4] gets all permissions from [PermissionLevel.LEVEL_1], [PermissionLevel.LEVEL_2], [PermissionLevel.LEVEL_3] plus their own
 * @param id The id which will be used to save the operators
 */
enum class PermissionLevel(val id: Int) {

    /**
     * Permission level 1
     */
    LEVEL_1(1),

    /**
     * Permission level 2
     */
    LEVEL_2(2),

    /**
     * Permission level 3
     */
    LEVEL_3(3),

    /**
     * Permission level 4. This permission level normally has all permissions
     */
    LEVEL_4(4);

    private val permissions = hashMapOf<String, Boolean>()

    /**
     * Whether the permission level has the specified [permission]
     */
    fun hasPermission(permission: String) = when (this) {
        LEVEL_1 -> permissions[permission] ?: false
        LEVEL_2 -> (LEVEL_1.permissions + permissions)[permission] ?: false
        LEVEL_3 -> (LEVEL_1.permissions + LEVEL_2.permissions + permissions)[permission] ?: false
        LEVEL_4 -> (LEVEL_1.permissions + LEVEL_2.permissions + LEVEL_3.permissions + permissions)[permission] ?: false
    }

    /**
     * Grant the permission level the specified [permission]
     * @param permission The permission to grant
     */
    fun grant(permission: String) {
        permissions[permission] = true
    }

    /**
     * Revoke the [permission] from the permission level
     * @param permission The permission to revoke
     */
    fun revoke(permission: String) {
        permissions[permission] = false
    }

    private fun rawPermissions() = permissions.filter { it.value }.keys.toList()

    /**
     * Returns a list with all permissions the permission level has
     */
    fun permissions() = when (this) {
        LEVEL_1 -> rawPermissions()
        LEVEL_2 -> rawPermissions() + LEVEL_1.rawPermissions()
        LEVEL_3 -> rawPermissions() + LEVEL_1.rawPermissions() + LEVEL_2.rawPermissions()
        LEVEL_4 -> rawPermissions() + LEVEL_1.rawPermissions() + LEVEL_2.rawPermissions() + LEVEL_3.rawPermissions()
    }

    companion object {

        /**
         * Gets the [PermissionLevel] from its id
         * @param id The permission level id
         */
        fun fromId(id: Int) = values().firstOrNull { it.id == id }

    }
}
