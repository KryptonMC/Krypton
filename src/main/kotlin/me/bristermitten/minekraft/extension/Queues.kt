package me.bristermitten.minekraft.extension

import java.util.*

fun <T : Any> Queue<T>.pollEach(function: (T) -> Unit)
{
    while (size != 0)
    {
        function(poll())
    }
}
