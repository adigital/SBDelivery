package ru.skillbranch.sbdelivery

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import ru.skillbranch.sbdelivery.extensions.shortFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.skillbranch.sbdelivery", appContext.packageName)
    }

    @Test
    fun dateTest() {

        println(
            SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("2021-06-28T08:22:18.057Z")
                ?.shortFormat()
        )

    }
}