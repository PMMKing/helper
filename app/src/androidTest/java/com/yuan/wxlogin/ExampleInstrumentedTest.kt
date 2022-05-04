package com.yuan.wxlogin

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junrunners.AndroidJUnit4

import org.junTest
import org.junrunner.RunWith

import org.junAssert.*

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
        assertEquals("com.yuan.wxlogin", appContext.packageName)
    }
}