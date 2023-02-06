/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event

/**
 * The root node of the server's event node tree.
 *
 * This is used to distinguish it from regular event nodes, to allow it to be
 * injected in to plugins that want to inject it in the same way as the event
 * manager could be injected.
 */
public interface GlobalEventNode : EventNode<Event>
