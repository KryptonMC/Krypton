package org.kryptonmc.krypton.world.fluid

import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.fluid.FluidHandler
import org.kryptonmc.krypton.world.fluid.handler.EmptyFluidHandler

fun Fluid.handler(): FluidHandler = KryptonFluidManager.handler(this) ?: EmptyFluidHandler
