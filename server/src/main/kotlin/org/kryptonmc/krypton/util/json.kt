package org.kryptonmc.krypton.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, T::class.java)

inline fun <reified T> GsonBuilder.registerTypeAdapter(adapter: Any): GsonBuilder = registerTypeAdapter(T::class.java, adapter)
