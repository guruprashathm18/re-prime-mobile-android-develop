package com.royalenfield.googleDirections;

import android.content.Context;

import com.bosch.softtec.components.midgard.core.directions.RouteProvider;
import com.bosch.softtec.components.midgard.directions.google.GoogleDirectionsApiCredentials;
import com.bosch.softtec.components.midgard.directions.google.GoogleDirectionsConfiguration;
import com.bosch.softtec.components.midgard.directions.google.GoogleRouteProvider;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;


/**
 * @author BOP1KOR on 5/2/2019.
 */
public final class RouteProviderGenerator {

    public static final RouteProviderGenerator INSTANCE;

    static {
        RouteProviderGenerator routeProviderGenerator = new RouteProviderGenerator();
        INSTANCE = routeProviderGenerator;
    }

    @NotNull
    public final RouteProvider getRouteProvider(@NotNull Context context,String authHeaders) {
        GoogleDirectionsConfiguration urlOverrideConfiguration =
                new GoogleDirectionsConfiguration.Builder().authenticationHeader(authHeaders).
                        googleDirectionsApiBaseUrl(REUtils.getMobileappbaseURLs().getTbtURL()).
                        googleDirectionsEndpoint("tbt/directions/json").
                        googleRoadsApiBaseUrl(REUtils.getMobileappbaseURLs().getTbtURL()).
                        googleSnapToRoadsEndpoint("tbt/roads").build();
        return (new GoogleRouteProvider(new GoogleDirectionsApiCredentials(REConstants.NAVIGATION_DUMMY_KEY),
                urlOverrideConfiguration));
    }
}

