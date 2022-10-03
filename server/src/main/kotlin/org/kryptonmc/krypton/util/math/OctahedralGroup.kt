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
package org.kryptonmc.krypton.util.math

import it.unimi.dsi.fastutil.booleans.BooleanArrayList
import it.unimi.dsi.fastutil.booleans.BooleanList
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.Directions
import org.spongepowered.math.matrix.Matrix3f
import java.util.Arrays
import java.util.EnumMap

enum class OctahedralGroup(
    private val groupName: String,
    private val permutation: SymmetricGroup3,
    private val invertX: Boolean,
    private val invertY: Boolean,
    private val invertZ: Boolean
) {

    IDENTITY("identity", SymmetricGroup3.P123, false, false, false),
    ROT_180_FACE_XY("rot_180_face_xy", SymmetricGroup3.P123, true, true, false),
    ROT_180_FACE_XZ("rot_180_face_xz", SymmetricGroup3.P123, true, false, true),
    ROT_180_FACE_YZ("rot_180_face_yz", SymmetricGroup3.P123, false, true, true),
    ROT_120_NNN("rot_120_nnn", SymmetricGroup3.P231, false, false, false),
    ROT_120_NNP("rot_120_nnp", SymmetricGroup3.P312, true, false, true),
    ROT_120_NPN("rot_120_npn", SymmetricGroup3.P312, false, true, true),
    ROT_120_NPP("rot_120_npp", SymmetricGroup3.P231, true, false, true),
    ROT_120_PNN("rot_120_pnn", SymmetricGroup3.P312, true, true, false),
    ROT_120_PNP("rot_120_pnp", SymmetricGroup3.P231, true, true, false),
    ROT_120_PPN("rot_120_ppn", SymmetricGroup3.P231, false, true, true),
    ROT_120_PPP("rot_120_ppp", SymmetricGroup3.P312, false, false, false),
    ROT_180_EDGE_XY_NEG("rot_180_edge_xy_neg", SymmetricGroup3.P213, true, true, true),
    ROT_180_EDGE_XY_POS("rot_180_edge_xy_pos", SymmetricGroup3.P213, false, false, true),
    ROT_180_EDGE_XZ_NEG("rot_180_edge_xz_neg", SymmetricGroup3.P321, true, true, true),
    ROT_180_EDGE_XZ_POS("rot_180_edge_xz_pos", SymmetricGroup3.P321, false, true, false),
    ROT_180_EDGE_YZ_NEG("rot_180_edge_yz_neg", SymmetricGroup3.P132, true, true, true),
    ROT_180_EDGE_YZ_POS("rot_180_edge_yz_pos", SymmetricGroup3.P132, true, false, false),
    ROT_90_X_NEG("rot_90_x_neg", SymmetricGroup3.P132, false, false, true),
    ROT_90_X_POS("rot_90_x_pos", SymmetricGroup3.P132, false, true, false),
    ROT_90_Y_NEG("rot_90_y_neg", SymmetricGroup3.P321, true, false, false),
    ROT_90_Y_POS("rot_90_y_pos", SymmetricGroup3.P321, false, false, true),
    ROT_90_Z_NEG("rot_90_z_neg", SymmetricGroup3.P213, false, true, false),
    ROT_90_Z_POS("rot_90_z_pos", SymmetricGroup3.P213, true, false, false),
    INVERSION("inversion", SymmetricGroup3.P123, true, true, true),
    INVERT_X("invert_x", SymmetricGroup3.P123, true, false, false),
    INVERT_Y("invert_y", SymmetricGroup3.P123, false, true, false),
    INVERT_Z("invert_z", SymmetricGroup3.P123, false, false, true),
    ROT_60_REF_NNN("rot_60_ref_nnn", SymmetricGroup3.P312, true, true, true),
    ROT_60_REF_NNP("rot_60_ref_nnp", SymmetricGroup3.P231, true, false, false),
    ROT_60_REF_NPN("rot_60_ref_npn", SymmetricGroup3.P231, false, false, true),
    ROT_60_REF_NPP("rot_60_ref_npp", SymmetricGroup3.P312, false, false, true),
    ROT_60_REF_PNN("rot_60_ref_pnn", SymmetricGroup3.P231, false, true, false),
    ROT_60_REF_PNP("rot_60_ref_pnp", SymmetricGroup3.P312, true, false, false),
    ROT_60_REF_PPN("rot_60_ref_ppn", SymmetricGroup3.P312, false, true, false),
    ROT_60_REF_PPP("rot_60_ref_ppp", SymmetricGroup3.P231, true, true, true),
    SWAP_XY("swap_xy", SymmetricGroup3.P213, false, false, false),
    SWAP_YZ("swap_yz", SymmetricGroup3.P132, false, false, false),
    SWAP_XZ("swap_xz", SymmetricGroup3.P321, false, false, false),
    SWAP_NEG_XY("swap_neg_xy", SymmetricGroup3.P213, true, true, false),
    SWAP_NEG_YZ("swap_neg_yz", SymmetricGroup3.P132, false, true, true),
    SWAP_NEG_XZ("swap_neg_xz", SymmetricGroup3.P321, true, false, true),
    ROT_90_REF_X_NEG("rot_90_ref_x_neg", SymmetricGroup3.P132, true, false, true),
    ROT_90_REF_X_POS("rot_90_ref_x_pos", SymmetricGroup3.P132, true, true, false),
    ROT_90_REF_Y_NEG("rot_90_ref_y_neg", SymmetricGroup3.P321, true, true, false),
    ROT_90_REF_Y_POS("rot_90_ref_y_pos", SymmetricGroup3.P321, false, true, true),
    ROT_90_REF_Z_NEG("rot_90_ref_z_neg", SymmetricGroup3.P213, false, true, true),
    ROT_90_REF_Z_POS("rot_90_ref_z_pos", SymmetricGroup3.P213, true, false, true);

    val transformation: Matrix3f = Matrix3fBuilder()
        .m00(if (invertX) -1F else 1F)
        .m11(if (invertY) -1F else 1F)
        .m22(if (invertZ) -1F else 1F)
        .mul(permutation.tranformation)
        .build()
    private var rotatedDirections: MutableMap<Direction, Direction>? = null

    private fun packInversions(): BooleanList = BooleanArrayList(booleanArrayOf(invertX, invertY, invertZ))

    fun compose(other: OctahedralGroup): OctahedralGroup = CAYLEY_TABLE[ordinal][other.ordinal]

    fun inverse(): OctahedralGroup = INVERSE_TABLE[ordinal]

    fun rotate(direction: Direction): Direction {
        if (rotatedDirections == null) {
            rotatedDirections = EnumMap(Direction::class.java)
            Direction.values().forEach {
                val axis = Direction.Axis.values()[permutation.permutation(it.axis.ordinal)]
                val axisDirection = if (inverts(axis)) it.axisDirection.opposite else it.axisDirection
                rotatedDirections!!.put(it, Directions.fromAxisAndDirection(axis, axisDirection))
            }
        }
        return rotatedDirections!!.get(direction)!!
    }

    fun inverts(axis: Direction.Axis): Boolean = when (axis) {
        Direction.Axis.X -> invertX
        Direction.Axis.Y -> invertY
        Direction.Axis.Z -> invertZ
    }

    fun rotate(frontAndTop: FrontAndTop): FrontAndTop = FrontAndTop.fromFrontAndTop(rotate(frontAndTop.front), rotate(frontAndTop.top))

    override fun toString(): String = groupName

    companion object {

        private val VALUES = values()
        private val CAYLEY_TABLE = createCayleyTable()
        // We use streams here because of findAny and toArray.
        private val INVERSE_TABLE: Array<OctahedralGroup> = Arrays.stream(VALUES)
            .map { group -> Arrays.stream(VALUES).filter { group.compose(it) == IDENTITY }.findAny().get() }
            .toArray { arrayOfNulls<OctahedralGroup>(it) }

        @JvmStatic
        private fun createCayleyTable(): Array<Array<OctahedralGroup>> {
            val inversions = VALUES.associateBy { Pair(it.permutation, it.packInversions()) }
            return Array(VALUES.size) { rowIndex ->
                val row = VALUES[rowIndex]
                val rowInversions = row.packInversions()
                Array(VALUES.size) { columnIndex ->
                    val column = VALUES[columnIndex]
                    val columnInversions = column.packInversions()
                    val symmetricGroup = column.permutation.compose(row.permutation)
                    val permutations = BooleanArrayList(3)
                    for (i in 0 until 3) {
                        permutations.add(rowInversions.getBoolean(i) xor columnInversions.getBoolean(row.permutation.permutation(i)))
                    }
                    inversions.get(Pair(symmetricGroup, permutations))!!
                }
            }
        }
    }
}
