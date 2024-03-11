/*
 * Copyright (c) 2019 Bosch SoftTec GmbH All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Bosch
 * SoftTec GmbH ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Bosch SoftTec (BSOT).
 *
 * BSOT MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. BSOT SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.bosch.softtec.components.nephele

/*
 * Copyright (c) 2019 Bosch SoftTec GmbH All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Bosch
 * SoftTec GmbH ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Bosch SoftTec (BSOT).
 *
 * BSOT MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. BSOT SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */


import android.app.Application
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.bosch.softtec.components.core.models.LatLng
import com.google.common.collect.ImmutableSet
import com.google.common.collect.Sets
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import org.robolectric.shadows.ShadowLooper

/**
 * .
 *
 * @param <T>
 * type of class under test
 * @author Andre Weber
 * @since 2018-02-01
 */
@RunWith(RobolectricTestRunner::class)
@Config(
        sdk = [Build.VERSION_CODES.O_MR1],
        application = Application::class,
        packageName = "com.bosch.softtec.components",
        shadows = [ShadowLog::class, ShadowLooper::class]
)
abstract class BaseTest<T : Any>
{
    @InjectMocks
    protected lateinit var classUnderTest: T

    protected val context: Context = spy(InstrumentationRegistry.getInstrumentation().context)

    protected val applicationContext: Context = ApplicationProvider.getApplicationContext()

    protected abstract fun initClassUnderTest(): T

    @Before
    @Throws(Exception::class)
    fun before()
    {
        classUnderTest = initClassUnderTest()
        MockitoAnnotations.initMocks(this)

        `when`(context.applicationContext).thenReturn(applicationContext)

        setUp()
    }

    open fun setUp()
    {
        // empty implementation for inheritance
    }

    @After
    fun after()
    {
        tearDown()
    }

    open fun tearDown()
    {
        // empty implementation for inheritance
    }

    /**
     * Gets the value of the field with the specified fieldName from the specified object.
     *
     * @param fieldName
     * the fieldName of the field.
     * @param from
     * the object to return the value of the field.
     * @return the value of the Field with the given fieldName from the given object.
     * @throws IllegalAccessException
     * if this field is not accessible.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalAccessException::class)
    fun <T : Any> getFieldValue(fieldName: String): T?
    {
        return classUnderTest.getFieldValue<Any>(fieldName) as T?
    }

    /**
     * Gets the value of the field with the specified fieldName from the specified object.
     *
     * @param fieldName
     * the fieldName of the field.
     * @param from
     * the object to return the value of the field.
     * @param value
     * the value to set.
     * @throws NoSuchFieldException
     * if the requested field can not be found.
     * @throws IllegalAccessException
     * if this field is not accessible.
     */
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun setFieldValue(fieldName: String, value: Any?)
    {
        classUnderTest.setFieldValue(fieldName, value)
    }

    protected inline fun <reified T : Any> mock(): T = mock(T::class.java)

    /**
     * Creates a non-null version of [Mockito.any], used instead of Mockitos ArgumentMatcher.
     * see: https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
     */
    protected fun <T> any(): T
    {
        Mockito.any<T>()
        return uninitialized()
    }

    protected fun anyInt(): Int
    {
        return Mockito.anyInt()
    }

    /**
     * Creates a non-null version of [Mockito.eq], used instead of Mockitos ArgumentMatcher.
     * see: https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791.
     *
     * ############################################ Attention ############################################
     * Terms like this are not allowed when using equals:
     * activeRoute?.routeOptions or
     * activeRoute.routeOptions
     *
     * instead you have to extract routeOptions to val and check it afterwards:
     *
     * val routeOptions = activeRoute?.routeOptions
     * verify(...).someMethod(eq(routeOptions))
     *
     * If done otherwise the following exception will be thrown:
     * InvalidUseOfMatchersException: Invalid use of argument matchers! 0 matchers expected, 1 recorded
     * ###################################################################################################
     */
    protected fun <T> eq(value: Any?): T
    {
        Mockito.eq(value)
        return uninitialized()
    }

    /**
     * Creates a non-null version of [Mockito.notNull], used instead of Mockitos ArgumentMatcher.
     * see: https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
     */
    protected fun <T> notNull(): T
    {
        Mockito.notNull<T>()
        return uninitialized()
    }


    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T

    protected inline fun <reified T : Any> mocks(n: Int): MutableList<T>
    {
        if (n < 1) throw IllegalArgumentException("Invalid parameter n = $n. n must be > 0")

        val mocks = mutableListOf<T>()
        for (i in 0 until n)
        {
            val mock = mock<T>()
            mocks.add(mock)
        }
        return mocks
    }

    protected fun callMethodByName(
            methodName: String,
            argumentClasses: Array<Class<out Any>>? = null,
            vararg arguments: Any
    )
    {
        classUnderTest.callMethodByName(methodName, argumentClasses, *arguments)
    }

    protected fun generatePolylinePoints(
            start: Int,
            end: Int,
            offset: Double = 0.0,
            stepLength: Int = 1
    ): List<LatLng>
    {
        val latLngs = arrayListOf<LatLng>()

        val endIndex = end - start
        for (i in 0..endIndex step stepLength)
        {
            val value = start + i * 1.0 + offset
            val latLng = LatLng(value, value)
            latLngs.add(latLng)
        }

        return latLngs
    }

    protected fun <T> symmetricalDifference(a: Collection<T>, b: Collection<T>): ImmutableSet<T>
    {
        val setA = a.toSet()
        val setB = b.toSet()

        val symmetricDifference = Sets.symmetricDifference(setA, setB)
        return symmetricDifference.immutableCopy()
    }
}
