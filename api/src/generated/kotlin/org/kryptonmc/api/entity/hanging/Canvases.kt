package org.kryptonmc.api.entity.hanging

import kotlin.String
import kotlin.Suppress
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Suppress("UndocumentedPublicProperty", "LargeClass")
@Catalogue(Canvas::class)
public object Canvases {

    // @formatter:off
    @JvmField public val KEBAB: Canvas = get("kebab")
    @JvmField public val AZTEC: Canvas = get("aztec")
    @JvmField public val ALBAN: Canvas = get("alban")
    @JvmField public val AZTEC2: Canvas = get("aztec2")
    @JvmField public val BOMB: Canvas = get("bomb")
    @JvmField public val PLANT: Canvas = get("plant")
    @JvmField public val WASTELAND: Canvas = get("wasteland")
    @JvmField public val POOL: Canvas = get("pool")
    @JvmField public val COURBET: Canvas = get("courbet")
    @JvmField public val SEA: Canvas = get("sea")
    @JvmField public val SUNSET: Canvas = get("sunset")
    @JvmField public val CREEBET: Canvas = get("creebet")
    @JvmField public val WANDERER: Canvas = get("wanderer")
    @JvmField public val GRAHAM: Canvas = get("graham")
    @JvmField public val MATCH: Canvas = get("match")
    @JvmField public val BUST: Canvas = get("bust")
    @JvmField public val STAGE: Canvas = get("stage")
    @JvmField public val VOID: Canvas = get("void")
    @JvmField public val SKULL_AND_ROSES: Canvas = get("skull_and_roses")
    @JvmField public val WITHER: Canvas = get("wither")
    @JvmField public val FIGHTERS: Canvas = get("fighters")
    @JvmField public val POINTER: Canvas = get("pointer")
    @JvmField public val PIGSCENE: Canvas = get("pigscene")
    @JvmField public val BURNING_SKULL: Canvas = get("burning_skull")
    @JvmField public val SKELETON: Canvas = get("skeleton")
    @JvmField public val DONKEY_KONG: Canvas = get("donkey_kong")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Canvas = Registries.CANVAS[Key.key(key)]!!
}
