package com.royalenfield.googleSearch;

import android.content.Context;

import com.bosch.softtec.components.midgard.core.search.SearchProvider;
import com.bosch.softtec.components.midgard.search.google.GoogleSearchApiCredentials;
import com.bosch.softtec.components.midgard.search.google.GoogleSearchConfiguration;
import com.bosch.softtec.components.midgard.search.google.GoogleSearchProvider;
import com.royalenfield.reprime.rest.web.RESSLClient;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import java.security.GeneralSecurityException;

import com.royalenfield.reprime.utils.RELog;

/**
 * @author BOP1KOR on 5/2/2019.
 */
public final class SearchProviderGenerator {
    public static final SearchProviderGenerator INSTANCE;

    static {
        SearchProviderGenerator searchProviderGenerator = new SearchProviderGenerator();
        INSTANCE = searchProviderGenerator;
    }

    @NotNull
    public final SearchProvider getSearchProvider(@NotNull Context context, String authHeader) {
        GoogleSearchApiCredentials googleSearchApiCredentials =
                new GoogleSearchApiCredentials(REConstants.NAVIGATION_DUMMY_KEY);
        GoogleSearchConfiguration googleSearchConfiguration = null;
        try {
        googleSearchConfiguration = new GoogleSearchConfiguration.Builder().
                authenticationHeader(authHeader).googlePlacesApiBaseUrl(REUtils.getMobileappbaseURLs().getTbtURL()).
                placeAutocompleteSearchEndpoint("tbt/place/autocomplete/json").
                placeDetailsEndpoint("tbt/place/details/json").
                placeNearbySearchEndpoint("tbt/place/nearbysearch/json").
                placeTextSearchEndpoint("tbt/place/textsearch/json").
                    sslConfiguration(RESSLClient.createSslConfiguration()).
        build();
        } catch (GeneralSecurityException e) {
            RELog.e(e);
        }
        return (new GoogleSearchProvider(context, googleSearchApiCredentials, googleSearchConfiguration));
    }
}
