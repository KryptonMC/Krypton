package org.kryptonmc.krypton.registry.block

import com.google.gson.JsonObject
import net.kyori.adventure.key.Key

class BlockData(
    val key: Key,
    private val main: JsonObject,
    private val override: JsonObject
) {

    val id = int("id")
    val stateId = int("stateId")
    val hardness = double("hardness")
    val resistance = double("explosionResistance")
    val friction = double("friction")
    val speedFactor = double("speedFactor")
    val jumpFactor = double("jumpFactor")
    val air = boolean("air")
    val solid = boolean("solid")
    val liquid = boolean("liquid")
    val blockEntity = boolean("blockEntity")
    val lightEmission = int("lightEmission")
    val occludes = boolean("occludes")
    val blocksMotion = boolean("blocksMotion")
    val flammable = boolean("flammable")
    val gravity = boolean("gravity")
    val translationKey = string("translationKey")

    private fun string(name: String) = element(name).asString
    private fun double(name: String) = element(name).asDouble
    private fun int(name: String) = element(name).asInt
    private fun boolean(name: String) = element(name).asBoolean
    private fun element(name: String) = override[name] ?: main[name]!!
}
