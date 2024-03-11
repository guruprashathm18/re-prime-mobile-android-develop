package com.bosch.softtec.components.nephele

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Andre Weber
 * @since 2019-08-27
 */
class BackendConfigurationTest : BaseTest<BackendConfiguration>() {
    private val backendUrl = "someBackendUrl"

    override fun initClassUnderTest(): BackendConfiguration {
        return BackendConfiguration(backendUrl)
    }

    @Test
    fun getBackendUrl() {
        assertEquals(backendUrl, classUnderTest.backendUrl)
    }
}
