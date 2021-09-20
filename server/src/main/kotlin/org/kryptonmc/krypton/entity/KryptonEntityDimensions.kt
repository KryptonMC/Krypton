package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.EntityDimensions
import org.kryptonmc.api.space.BoundingBox

@JvmRecord
data class KryptonEntityDimensions(
    override val width: Float,
    override val height: Float,
    override val isFixed: Boolean
) : EntityDimensions {

    override fun scale(width: Float, height: Float): EntityDimensions {
        if (isFixed || width == 1F && height == 1F) return this
        return KryptonEntityDimensions(this.width * width, this.height * height, false)
    }

    override fun toBoundingBox(x: Double, y: Double, z: Double): BoundingBox {
        val center = width / 2.0
        return BoundingBox(
            x - center,
            y,
            z - center,
            x + center,
            y + height,
            z + center
        )
    }

    object Factory : EntityDimensions.Factory {

        override fun scalable(width: Float, height: Float): EntityDimensions = KryptonEntityDimensions(width, height, false)

        override fun fixed(width: Float, height: Float): EntityDimensions = KryptonEntityDimensions(width, height, true)
    }
}
