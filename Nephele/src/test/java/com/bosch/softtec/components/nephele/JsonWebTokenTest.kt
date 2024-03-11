package com.bosch.softtec.components.nephele

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Andre Weber
 * @since 2020-01-21
 */
class JsonWebTokenTest : BaseTest<JsonWebToken>() {
    private val token = "someToken"

    override fun initClassUnderTest(): JsonWebToken {
        return JsonWebToken(token)
    }

    @Test
    fun getToken() {
        assertEquals(token, classUnderTest.token)
    }
}
