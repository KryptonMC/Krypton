/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.util.monitoring.jmx

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.util.logger
import java.lang.management.ManagementFactory
import javax.management.Attribute
import javax.management.AttributeList
import javax.management.DynamicMBean
import javax.management.MBeanAttributeInfo
import javax.management.MBeanInfo
import javax.management.ObjectName

/**
 * Used to publish some statistics to the Java Management Extensions (JMX) framework
 */
class KryptonStatistics(private val server: KryptonServer) : DynamicMBean {

    private val attributeDescriptionByName = mapOf(
        "tickTimes" to AttributeDescription("tickTimes", server::tickTimes, "Historical tick times in ms", LongArray::class.java),
        "averageTickTime" to AttributeDescription("averageTickTime", server::averageTickTime, "Average tick time in ms", Long::class.java)
    )
    private val beanInfo: MBeanInfo

    init {
        val attributes = attributeDescriptionByName.values.map(AttributeDescription::asAttributeInfo).toTypedArray()
        beanInfo = MBeanInfo(this::class.simpleName, "Simple metrics for Krypton", attributes, null, null, emptyArray())
    }

    override fun getMBeanInfo() = beanInfo

    override fun getAttribute(attribute: String) = attributeDescriptionByName[attribute]?.getter?.invoke()

    override fun setAttribute(attribute: Attribute?) = Unit

    override fun getAttributes(attributes: Array<out String>) =
        AttributeList(attributes.mapNotNull(attributeDescriptionByName::get).map { Attribute(it.name, it.getter()) })

    override fun setAttributes(attributes: AttributeList?) = AttributeList()

    override fun invoke(actionName: String?, params: Array<out Any>?, signature: Array<out String>?) = null

    companion object {

        private val PLATFORM_BEAN = ManagementFactory.getPlatformMBeanServer()
        private val LOGGER = logger<KryptonStatistics>()

        fun register(server: KryptonServer) {
            try {
                PLATFORM_BEAN.registerMBean(KryptonStatistics(server), ObjectName("org.kryptonmc.krypton:type=KryptonServer"))
            } catch (exception: Exception) {
                LOGGER.warn("Failed to register server as a JMX bean", exception)
            }
        }
    }
}

private data class AttributeDescription(
    val name: String,
    val getter: () -> Any,
    val description: String,
    val type: Class<*>
) {

    fun asAttributeInfo() = MBeanAttributeInfo(name, type.simpleName, description, true, false, false)
}
