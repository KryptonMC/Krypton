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
package org.kryptonmc.krypton.auth.requests

import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.auth.KryptonGameProfile
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.CompletableFuture

interface MojangApiService {

    @GET("users/profiles/minecraft/{name}")
    fun profile(@Path("name") name: String): Call<KryptonGameProfile>
}

object ApiService {

    private const val API_BASE_URL = "https://api.mojang.com/"

    private val apiService = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GSON))
        .build()
        .create<MojangApiService>()

    fun profile(name: String): CompletableFuture<KryptonGameProfile?> = CompletableFuture.supplyAsync { apiService.profile(name).execute().body() }
}
