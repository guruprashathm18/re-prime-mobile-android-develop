package com.bosch.softtec.micro.tenzing.client.model

import com.bosch.softtec.components.nephele.BaseTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @author Andre Weber
 * @since 2019-12-13
 */
class CircleTest : BaseTest<Circle>() {
    override fun initClassUnderTest(): Circle {
        return Circle()
    }

    @Test
    @Suppress("USELESS_IS_CHECK")
    fun classInheritsFromOneOfBBoxCircle() {
        assertTrue(classUnderTest is OneOfBBoxCircle)
    }
}
