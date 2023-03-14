/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.network.netty

import io.netty.channel.Channel

/**
 * A listener that will be invoked when a channel is initialised.
 *
 * While this technically is not public API, as it is part of the server, and
 * it may be removed without warning, there is a very low chance that this will
 * be removed, as it was added specifically for plugins that depend on the
 * server and need to add handlers to the pipeline.
 */
fun interface ChannelInitializeListener {

    fun onInitialize(channel: Channel)
}
