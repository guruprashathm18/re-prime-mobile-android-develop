package com.bosch.softtec.micro.tenzing.client.model

import com.bosch.softtec.components.nephele.BaseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @author Andre Weber
 * @since 2020-02-14
 */
// this class mainly tests if "TripBodyWithImage" was correctly renamed to "TripBodyWithImageUrl"
class TripBodyWithImageUrlTest : BaseTest<TripBodyWithImageUrl>() {
    override fun initClassUnderTest(): TripBodyWithImageUrl = TripBodyWithImageUrl()
        .name("someName")
        .description("someDescription")
        .addTrackItem(GpsPoint().latitude(10.0).longitude(20.0))
        .addDirectionsItem(GpsPoint().latitude(10.0).longitude(20.0))

    @Test
    fun testAddImageItem() {
        val url = "http://www.someUrl.de/image.png"
        classUnderTest.addImageUrlItem(url)

        assertEquals(1, classUnderTest.imageUrls.size)
        assertTrue(classUnderTest.imageUrls.contains(url))
    }

    @Test
    fun testAddImageItemWithEmptyString() {
        val url = ""
        classUnderTest.addImageUrlItem(url)

        assertEquals(0, classUnderTest.imageUrls.size)
    }

    @Test
    fun testSetImages() {
        val url1 = "http://www.someUrl.de/image.png"
        val url2 = "http://www.someOtherUrl.de/image.png"
        classUnderTest.images(listOf(url1, url2))

        assertEquals(2, classUnderTest.imageUrls.size)
        assertTrue(classUnderTest.imageUrls.contains(url1))
        assertTrue(classUnderTest.imageUrls.contains(url2))
    }

}
