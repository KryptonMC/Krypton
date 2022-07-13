/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.data.provider.entity

import org.kryptonmc.krypton.data.provider.DataProviderRegistrarBuilder

object EntityDataProviders : DataProviderRegistrarBuilder() {

    override fun registerProviders() {
        AcceleratingProjectileData.register(registrar)
        AgeableData.register(registrar)
        AnimalData.register(registrar)
        AreaEffectCloudData.register(registrar)
        ArmorStandData.register(registrar)
        ArrowLikeData.register(registrar)
        AxolotlData.register(registrar)
        BatData.register(registrar)
        BeeData.register(registrar)
        BoatData.register(registrar)
        CatData.register(registrar)
        ChickenData.register(registrar)
        CommandBlockMinecartData.register(registrar)
        CreeperData.register(registrar)
        DolphinData.register(registrar)
        EntityData.register(registrar)
        ExperienceOrbData.register(registrar)
        FireworkRocketData.register(registrar)
        FishingHookData.register(registrar)
        FoxData.register(registrar)
        FurnaceMinecartData.register(registrar)
        GlowSquidData.register(registrar)
        GoatData.register(registrar)
        HangingEntityData.register(registrar)
        LargeFireballData.register(registrar)
        LivingEntityData.register(registrar)
        MinecartLikeData.register(registrar)
        MobData.register(registrar)
        MooshroomData.register(registrar)
        OcelotData.register(registrar)
        PaintingData.register(registrar)
        PandaData.register(registrar)
        ParrotData.register(registrar)
        PigData.register(registrar)
        PlayerData.register(registrar)
        PolarBearData.register(registrar)
        ProjectileData.register(registrar)
        RabbitData.register(registrar)
        SheepData.register(registrar)
        ShulkerBulletData.register(registrar)
        TamableData.register(registrar)
        TridentData.register(registrar)
        TropicalFishData.register(registrar)
        TurtleData.register(registrar)
        WitherSkullData.register(registrar)
        WolfData.register(registrar)
        ZombieData.register(registrar)
    }
}
