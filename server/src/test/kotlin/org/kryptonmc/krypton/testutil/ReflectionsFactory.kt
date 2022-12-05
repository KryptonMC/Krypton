package org.kryptonmc.krypton.testutil

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder

object ReflectionsFactory {

    @JvmStatic
    fun subTypesIn(pkg: String): Reflections = Reflections(ConfigurationBuilder().forPackage(pkg).addScanners(Scanners.SubTypes))
}
