package org.kryptonmc.krypton.auth

import com.google.gson.stream.JsonReader
import kotlinx.collections.immutable.persistentListOf
import nl.jqno.equalsverifier.EqualsVerifier
import org.junit.jupiter.api.Test
import java.io.StringReader
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.concurrent.atomic.AtomicLong
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProfileHolderTests {

    @Test
    fun `verify equals and hashcode`() {
        EqualsVerifier.forClass(ProfileHolder::class.java)
            .withIgnoredFields("lastAccess")
            .verify()
    }

    @Test
    fun `verify equal objects compare equal`() {
        val time = ZonedDateTime.now()
        val holder1 = ProfileHolder(DUMMY_PROFILE, time)
        val holder2 = ProfileHolder(DUMMY_PROFILE, time)
        assertEquals(0, holder1.compareTo(holder1))
        assertEquals(0, holder2.compareTo(holder2))
        assertEquals(0, holder1.compareTo(holder2))
        assertEquals(0, holder2.compareTo(holder1))
    }

    @Test
    fun `verify newer last access less than older last access`() {
        val time = ZonedDateTime.now()
        val holder1 = ProfileHolder(DUMMY_PROFILE, time)
        val holder2 = ProfileHolder(DUMMY_PROFILE, time)
        holder1.updateAndGetProfile(AtomicLong())
        assertEquals(-1, holder1.compareTo(holder2))
        assertEquals(1, holder2.compareTo(holder1))
    }

    @Test
    fun `verify older last access greater than newer last access`() {
        val time = ZonedDateTime.now()
        val holder1 = ProfileHolder(DUMMY_PROFILE, time)
        val holder2 = ProfileHolder(DUMMY_PROFILE, time)
        holder2.updateAndGetProfile(AtomicLong())
        assertEquals(1, holder1.compareTo(holder2))
        assertEquals(-1, holder2.compareTo(holder1))
    }

    @Test
    fun `verify to string consistency`() {
        val time = ZonedDateTime.now()
        assertEquals("ProfileHolder(profile=$DUMMY_PROFILE, expiryDate=$time)", ProfileHolder(DUMMY_PROFILE, time).toString())
    }

    @Test
    fun `test reading empty object with no properties`() {
        assertNull(ProfileHolder.read(JsonReader(StringReader("{}"))))
    }

    @Test
    fun `test reading object with all properties`() {
        val id = UUID.fromString("12345678-1234-1234-1234-123456789012")
        val name = "test"
        val timeText = "2022-05-12 16:21:25 +0000"
        val time = ZonedDateTime.parse(timeText, DATE_FORMATTER)
        val input = """
            {
                "uuid": "$id",
                "name": "$name",
                "expiresOn": "$timeText"
            }
        """.trimIndent()
        val holder = ProfileHolder(KryptonGameProfile(name, id, persistentListOf()), time)
        assertEquals(holder, ProfileHolder.read(JsonReader(StringReader(input))))
    }

    companion object {

        private val DUMMY_PROFILE = KryptonGameProfile("test", UUID.randomUUID(), persistentListOf())
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
    }
}
