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
package org.kryptonmc.krypton.locale

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.translation.GlobalTranslator
import net.kyori.adventure.translation.TranslationRegistry
import net.kyori.adventure.translation.Translator
import net.kyori.adventure.util.UTF8ResourceBundleControl
import org.kryptonmc.krypton.CURRENT_DIRECTORY
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale
import java.util.PropertyResourceBundle
import java.util.ResourceBundle
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

object TranslationManager {

    private val DEFAULT_LOCALE: Locale = Locale.ENGLISH
    val TRANSLATIONS_DIRECTORY: Path = CURRENT_DIRECTORY.resolve("translations")
    private val LOGGER = logger<TranslationManager>()

    private val installed = ConcurrentHashMap.newKeySet<Locale>()
    private lateinit var registry: TranslationRegistry

    @Volatile
    var locale = DEFAULT_LOCALE

    fun reload(locale: Locale = this.locale) {
        this.locale = locale
        if (::registry.isInitialized) {
            GlobalTranslator.get().removeSource(registry)
            installed.clear()
        }

        registry = TranslationRegistry.create(Key.key("krypton", "main"))
        registry.defaultLocale(DEFAULT_LOCALE)

        loadCustom()
        loadBase()

        GlobalTranslator.get().addSource(registry)
    }

    private fun loadBase() {
        val bundle = ResourceBundle.getBundle("krypton", DEFAULT_LOCALE, UTF8ResourceBundleControl.get())
        try {
            registry.registerAll(DEFAULT_LOCALE, bundle, false)
        } catch (exception: IllegalArgumentException) {
            LOGGER.warn("Error loading default locale file", exception)
        }
    }

    private fun loadCustom() {
        val translationFiles = try {
            Files.list(TRANSLATIONS_DIRECTORY).use { stream ->
                stream.filter { it.fileName.toString().endsWith(".properties") }.collect(Collectors.toList())
            }
        } catch (exception: IOException) {
            emptyList()
        }

        val loaded = mutableMapOf<Locale, ResourceBundle>()
        translationFiles.forEach { file ->
            try {
                loadCustomTranslationFile(file)?.let { loaded += it }
            } catch (exception: Exception) {
                LOGGER.warn("Error loading locale file ${file.fileName}", exception)
            }
        }

        loaded.forEach { (locale, bundle) ->
            val localeWithoutCountry = Locale(locale.language)
            if (locale != localeWithoutCountry && localeWithoutCountry != DEFAULT_LOCALE && installed.add(localeWithoutCountry)) {
                registry.registerAll(localeWithoutCountry, bundle, false)
            }
        }
    }

    private fun loadCustomTranslationFile(file: Path): Pair<Locale, ResourceBundle>? {
        val fileName = file.fileName.toString()
        val localeString = fileName.substring(0, fileName.length - ".properties".length)
        val locale = localeString.toLocale()

        if (locale == null) {
            LOGGER.warn("Unknown locale '$localeString' - unable to register.")
            return null
        }

        val bundle = try {
            Files.newBufferedReader(file, Charsets.UTF_8).use { PropertyResourceBundle(it) }
        } catch (exception: IOException) {
            LOGGER.warn("Error loading locale file $localeString", exception)
            return null
        }

        try {
            registry.registerAll(locale, bundle, true)
        } catch (exception: Exception) {
            return null
        }
        installed += locale
        return locale to bundle
    }

    fun render(component: Component) = GlobalTranslator.render(component, locale)
}

fun String.toLocale() = Translator.parseLocale(this)
