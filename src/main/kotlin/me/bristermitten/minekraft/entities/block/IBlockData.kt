package me.bristermitten.minekraft.entities.block

interface IBlockData {

    val states: Collection<IBlockState<*>>

    val statesMap: Map<IBlockState<*>, Comparable<*>>

    val block: Block

    fun <T : Comparable<T>> get(state: IBlockState<T>): T

    fun <T : Comparable<T>, V : T> set(state: IBlockState<T>, value: V): IBlockData

    fun <T : Comparable<T>> a(var1: IBlockState<T>): IBlockData
}