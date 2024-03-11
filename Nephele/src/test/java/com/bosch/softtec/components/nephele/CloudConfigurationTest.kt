package com.bosch.softtec.components.nephele

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Andre Weber
 * @since 2019-08-27
 */
class CloudConfigurationTest : BaseTest<CloudConfiguration>() {
    private val backendConfiguration = mock<BackendConfiguration>()

    override fun initClassUnderTest(): CloudConfiguration {
        return CloudConfiguration(backendConfiguration)
    }

    @Test
    fun getBackendConfiguration() {
        assertEquals(backendConfiguration, classUnderTest.backendConfiguration)
    }
}
