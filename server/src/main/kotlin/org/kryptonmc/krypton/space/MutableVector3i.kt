package org.kryptonmc.krypton.space

class MutableVector3i(
    var x: Int = 0,
    var y: Int = 0,
    var z: Int = 0
) {

    fun set(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }
}
