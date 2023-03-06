package org.kryptonmc.krypton.entity.ai.pathfinding

import com.extollit.gaming.ai.path.model.ColumnarOcclusionFieldList
import com.extollit.gaming.ai.path.model.IBlockDescription
import com.extollit.gaming.ai.path.model.IColumnarSpace
import com.extollit.gaming.ai.path.model.IInstanceSpace
import org.kryptonmc.krypton.world.chunk.KryptonChunk

class KryptonColumnarSpace(private val instanceSpace: KryptonInstanceSpace, private val chunk: KryptonChunk) : IColumnarSpace {

    private val occlusionFieldList = ColumnarOcclusionFieldList(this)

    override fun blockAt(x: Int, y: Int, z: Int): IBlockDescription = KryptonHydrazineBlock.get(chunk.getBlock(x, y, z))

    override fun metaDataAt(x: Int, y: Int, z: Int): Int = 0

    override fun occlusionFields(): ColumnarOcclusionFieldList = occlusionFieldList

    override fun instance(): IInstanceSpace = instanceSpace
}
