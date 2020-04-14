package me.bristermitten.minekraft.extension

import java.util.logging.Logger
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


fun <T : Any> logger(clazz: Class<T>) = LoggerProperty(clazz)
class LoggerProperty<P : Any>(clazz: Class<P>) : ReadOnlyProperty<P, Logger> {

    private val logger = Logger.getLogger(clazz.name)
    override fun getValue(thisRef: P, property: KProperty<*>): Logger {
        return logger
    }
}
