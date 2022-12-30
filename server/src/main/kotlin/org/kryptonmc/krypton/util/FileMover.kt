package org.kryptonmc.krypton.util

import org.apache.logging.log4j.LogManager
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.BooleanSupplier

object FileMover {

    private val LOGGER = LogManager.getLogger()

    @JvmStatic
    fun safeReplaceFile(old: Path, new: Path, backup: Path) {
        if (Files.exists(old) && !runWithRetries(10, "create backup $backup", deleter(backup), renamer(old, backup), createdCheck(backup))) return
        // If we get past the above, we know that we have a backup if we need one.
        // So we now remove the old file we don't need anymore
        if (!runWithRetries(10, "remove old $old", deleter(old), deletedCheck(old))) return
        // If we get past the above, we successfully removed the old file
        // So we now move the new file to the old file
        if (runWithRetries(10, "replace $old with $new", renamer(new, old), createdCheck(old))) return
        // If we get past the above, we failed to replace the old file with the new one
        // So we need to restore from backup
        runWithRetries(10, "restore $old from $backup", renamer(backup, old), createdCheck(old))
    }

    @JvmStatic
    private fun renamer(source: Path, target: Path): BooleanSupplier = createAction("rename $source to $target") {
        try {
            Files.move(source, target)
            true
        } catch (exception: IOException) {
            LOGGER.error("Failed to rename $source to $target!", exception)
            false
        }
    }

    @JvmStatic
    private fun deleter(path: Path): BooleanSupplier = createAction("delete old $path") {
        try {
            Files.deleteIfExists(path)
            true
        } catch (exception: IOException) {
            LOGGER.warn("Failed to delete $path.", exception)
            false
        }
    }

    @JvmStatic
    private fun deletedCheck(path: Path): BooleanSupplier = createAction("verify that $path was deleted") { !Files.exists(path) }

    @JvmStatic
    private fun createdCheck(path: Path): BooleanSupplier = createAction("verify that $path was created") { Files.isRegularFile(path) }

    @JvmStatic
    private inline fun createAction(name: String, crossinline action: () -> Boolean): BooleanSupplier = object : BooleanSupplier {
        override fun getAsBoolean(): Boolean = action()

        override fun toString(): String = name
    }

    @JvmStatic
    private fun runWithRetries(maxTries: Int, actionName: String, vararg actions: BooleanSupplier): Boolean {
        for (i in 0 until maxTries) {
            if (executeInSequence(actions)) return true
            LOGGER.error("Failed to $actionName, retrying ($i / $maxTries)")
        }
        LOGGER.error("Failed to $actionName! Aborting. Progress may be lost.")
        return false
    }

    @JvmStatic
    private fun executeInSequence(actions: Array<out BooleanSupplier>): Boolean {
        for (action in actions) {
            if (!action.asBoolean) {
                LOGGER.warn("Failed to execute $action.")
                return false
            }
        }
        return true
    }
}
