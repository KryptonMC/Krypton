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
package org.kryptonmc.krypton.command

import com.mojang.brigadier.exceptions.BuiltInExceptionProvider
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import org.kryptonmc.krypton.command.arguments.CommandExceptions

// Built-in types using vanilla's translation keys
object BrigadierExceptions : BuiltInExceptionProvider {

    // Out of range/incorrect errors
    private val INTEGER_TOO_SMALL = CommandExceptions.dynamic2("argument.integer.low")
    private val INTEGER_TOO_LARGE = CommandExceptions.dynamic2("argument.integer.big")
    private val LONG_TOO_SMALL = CommandExceptions.dynamic2("argument.long.low")
    private val LONG_TOO_LARGE = CommandExceptions.dynamic2("argument.long.big")
    private val FLOAT_TOO_SMALL = CommandExceptions.dynamic2("argument.float.low")
    private val FLOAT_TOO_LARGE = CommandExceptions.dynamic2("argument.float.big")
    private val DOUBLE_TOO_SMALL = CommandExceptions.dynamic2("argument.double.low")
    private val DOUBLE_TOO_LARGE = CommandExceptions.dynamic2("argument.double.big")
    private val LITERAL_INCORRECT = CommandExceptions.dynamic("argument.literal.incorrect")

    // Reader invalid/expected errors
    private val READER_INVALID_ESCAPE = CommandExceptions.dynamic("parsing.quote.escape")
    private val READER_INVALID_BOOLEAN = CommandExceptions.dynamic("parsing.bool.invalid")
    private val READER_INVALID_INTEGER = CommandExceptions.dynamic("parsing.int.invalid")
    private val READER_INVALID_LONG = CommandExceptions.dynamic("parsing.long.invalid")
    private val READER_INVALID_FLOAT = CommandExceptions.dynamic("parsing.float.invalid")
    private val READER_INVALID_DOUBLE = CommandExceptions.dynamic("parsing.double.invalid")
    private val READER_EXPECTED_BOOLEAN = CommandExceptions.simple("parsing.bool.expected")
    private val READER_EXPECTED_INTEGER = CommandExceptions.simple("parsing.int.expected")
    private val READER_EXPECTED_LONG = CommandExceptions.simple("parsing.long.expected")
    private val READER_EXPECTED_FLOAT = CommandExceptions.simple("parsing.float.expected")
    private val READER_EXPECTED_DOUBLE = CommandExceptions.simple("parsing.double.expected")
    private val READER_EXPECTED_START_OF_QUOTE = CommandExceptions.simple("parsing.quote.expected.start")
    private val READER_EXPECTED_END_OF_QUOTE = CommandExceptions.simple("parsing.quote.expected.end")
    private val READER_EXPECTED_SYMBOL = CommandExceptions.dynamic("parsing.expected")

    // Dispatcher errors
    private val DISPATCHER_UNKNOWN_COMMAND = CommandExceptions.simple("command.unknown.command")
    private val DISPATCHER_UNKNOWN_ARGUMENT = CommandExceptions.simple("command.unknown.argument")
    private val DISPATCHER_EXPECTED_SEPARATOR = CommandExceptions.simple("command.expected.separator")
    private val DISPATCHER_PARSE_EXCEPTION = CommandExceptions.dynamic("command.exception")

    override fun integerTooLow(): Dynamic2CommandExceptionType = INTEGER_TOO_SMALL

    override fun integerTooHigh(): Dynamic2CommandExceptionType = INTEGER_TOO_LARGE

    override fun longTooLow(): Dynamic2CommandExceptionType = LONG_TOO_SMALL

    override fun longTooHigh(): Dynamic2CommandExceptionType = LONG_TOO_LARGE

    override fun floatTooLow(): Dynamic2CommandExceptionType = FLOAT_TOO_SMALL

    override fun floatTooHigh(): Dynamic2CommandExceptionType = FLOAT_TOO_LARGE

    override fun doubleTooLow(): Dynamic2CommandExceptionType = DOUBLE_TOO_SMALL

    override fun doubleTooHigh(): Dynamic2CommandExceptionType = DOUBLE_TOO_LARGE

    override fun literalIncorrect(): DynamicCommandExceptionType = LITERAL_INCORRECT

    override fun readerInvalidEscape(): DynamicCommandExceptionType = READER_INVALID_ESCAPE

    override fun readerInvalidBool(): DynamicCommandExceptionType = READER_INVALID_BOOLEAN

    override fun readerInvalidInt(): DynamicCommandExceptionType = READER_INVALID_INTEGER

    override fun readerInvalidLong(): DynamicCommandExceptionType = READER_INVALID_LONG

    override fun readerInvalidFloat(): DynamicCommandExceptionType = READER_INVALID_FLOAT

    override fun readerInvalidDouble(): DynamicCommandExceptionType = READER_INVALID_DOUBLE

    override fun readerExpectedBool(): SimpleCommandExceptionType = READER_EXPECTED_BOOLEAN

    override fun readerExpectedInt(): SimpleCommandExceptionType = READER_EXPECTED_INTEGER

    override fun readerExpectedLong(): SimpleCommandExceptionType = READER_EXPECTED_LONG

    override fun readerExpectedFloat(): SimpleCommandExceptionType = READER_EXPECTED_FLOAT

    override fun readerExpectedDouble(): SimpleCommandExceptionType = READER_EXPECTED_DOUBLE

    override fun readerExpectedStartOfQuote(): SimpleCommandExceptionType = READER_EXPECTED_START_OF_QUOTE

    override fun readerExpectedEndOfQuote(): SimpleCommandExceptionType = READER_EXPECTED_END_OF_QUOTE

    override fun readerExpectedSymbol(): DynamicCommandExceptionType = READER_EXPECTED_SYMBOL

    override fun dispatcherUnknownCommand(): SimpleCommandExceptionType = DISPATCHER_UNKNOWN_COMMAND

    override fun dispatcherUnknownArgument(): SimpleCommandExceptionType = DISPATCHER_UNKNOWN_ARGUMENT

    override fun dispatcherExpectedArgumentSeparator(): SimpleCommandExceptionType = DISPATCHER_EXPECTED_SEPARATOR

    override fun dispatcherParseException(): DynamicCommandExceptionType = DISPATCHER_PARSE_EXCEPTION
}
