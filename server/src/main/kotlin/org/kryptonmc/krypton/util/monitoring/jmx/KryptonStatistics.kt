package org.kryptonmc.krypton.util.monitoring.jmx

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.extension.logger
import java.lang.management.ManagementFactory
import javax.management.*

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

        private val LOGGER = logger<KryptonStatistics>()

        fun register(server: KryptonServer) {
            try {
                ManagementFactory.getPlatformMBeanServer().registerMBean(KryptonStatistics(server), ObjectName("org.kryptonmc.krypton:type=KryptonServer"))
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