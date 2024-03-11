package com.royalenfield.reprime.ui.home.connected.locatemotorcycle.model.response;

import com.google.gson.Gson;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeviceLatestLocationTest extends TestCase {

    private DeviceLatestLocation deviceLatestLocation = null;
    private String ASSET_BASE_PATH = "../app/src/main/assets/";

    @Before
    public void setUp() throws IOException {
        deviceLatestLocation = new Gson().fromJson(
                readJsonFile("device_latest_location.json"), ConnectedResponse.class)
                .getData().getDeviceLatestLocation();
    }

    @Test
    public void testIsOffline() {
        boolean expected = false;
        deviceLatestLocation.setOffline(expected);
        boolean actual = deviceLatestLocation.isOffline();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocateDeviceDataNotNull() {
        Assert.assertNotNull(deviceLatestLocation);
    }

    @Test
    public void testLocateDeviceLatestData() {
        Assert.assertTrue(deviceLatestLocation.isOffline());
        Assert.assertEquals("TS07UF6212", deviceLatestLocation.getVehicleNumber());
        Assert.assertEquals("Bike", deviceLatestLocation.getVehicleType());
        Assert.assertTrue(deviceLatestLocation.getLatitude() != 0.0);
        Assert.assertTrue(deviceLatestLocation.getLongitude() != 0.0);
    }

    @After
    public void tearDown() {
        deviceLatestLocation = null;
    }

    private String readJsonFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(ASSET_BASE_PATH
                        + filename)));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }

        return sb.toString();
    }

}