package org.kryptonmc.krypton.util.datafix.schema

import com.mojang.datafixers.DSL
import com.mojang.datafixers.DSL.list
import com.mojang.datafixers.DSL.optionalFields
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import org.kryptonmc.krypton.util.datafix.References
import java.util.function.Supplier

class V100(versionKey: Int, parent: Schema?) : Schema(versionKey, parent) {

    override fun registerEntities(schema: Schema): Map<String, Supplier<TypeTemplate>> = super.registerEntities(schema).apply {
        schema.registerMob(this, "ArmorStand")
        schema.registerMob(this, "Creeper")
        schema.registerMob(this, "Skeleton")
        schema.registerMob(this, "Spider")
        schema.registerMob(this, "Giant")
        schema.registerMob(this, "Zombie")
        schema.registerMob(this, "Slime")
        schema.registerMob(this, "Ghast")
        schema.registerMob(this, "PigZombie")
        schema.register(this, "Enderman") { _ -> optionalFields("carried", References.BLOCK_NAME.`in`(schema), schema.equipment()) }
        schema.registerMob(this, "CaveSpider")
        schema.registerMob(this, "Silverfish")
        schema.registerMob(this, "Blaze")
        schema.registerMob(this, "LavaSlime")
        schema.registerMob(this, "EnderDragon")
        schema.registerMob(this, "WitherBoss")
        schema.registerMob(this, "Bat")
        schema.registerMob(this, "Witch")
        schema.registerMob(this, "Endermite")
        schema.registerMob(this, "Guardian")
        schema.registerMob(this, "Pig")
        schema.registerMob(this, "Sheep")
        schema.registerMob(this, "Cow")
        schema.registerMob(this, "Chicken")
        schema.registerMob(this, "Squid")
        schema.registerMob(this, "Wolf")
        schema.registerMob(this, "MushroomCow")
        schema.registerMob(this, "SnowMan")
        schema.registerMob(this, "Ozelot")
        schema.registerMob(this, "VillagerGolem")
        schema.register(this, "EntityHorse") { _ ->
            optionalFields(
                "Items", list(References.ITEM_STACK.`in`(schema)),
                "ArmorItem", References.ITEM_STACK.`in`(schema),
                "SaddleItem", References.ITEM_STACK.`in`(schema),
                schema.equipment()
            )
        }
        schema.registerMob(this, "Rabbit")
        schema.register(this, "Villager") { _ ->
            optionalFields(
                "Inventory", list(References.ITEM_STACK.`in`(schema)),
                "Offers", optionalFields("Recipes", list(
                    optionalFields(
                    "buy", References.ITEM_STACK.`in`(schema),
                    "buyB", References.ITEM_STACK.`in`(schema),
                    "sell", References.ITEM_STACK.`in`(schema),
                    )
                )),
                schema.equipment()
            )
        }
        schema.registerMob(this, "Shulker")
        schema.registerSimple(this, "AreaEffectCloud")
        schema.registerSimple(this, "ShulkerBullet")
    }

    override fun registerTypes(schema: Schema, entityTypes: Map<String, Supplier<TypeTemplate>>, blockEntityTypes: Map<String, Supplier<TypeTemplate>>) = with(schema) {
        super.registerTypes(this, entityTypes, blockEntityTypes)
        registerType(false, References.STRUCTURE) {
            optionalFields(
                "entities", list(optionalFields("nbt", References.ENTITY_TREE.`in`(this))),
                "blocks", list(optionalFields("nbt", References.BLOCK_ENTITY.`in`(this))),
                "palette", list(References.BLOCK_STATE.`in`(this))
            )
        }
        registerType(false, References.BLOCK_STATE, DSL::remainder)
    }
}
