package com.royalenfield.googleSearch;

import android.content.Context;

import com.bosch.softtec.components.midgard.core.search.SearchProvider;
import com.royalenfield.reprime.utils.REUtils;

/**
 * @author BOP1KOR on 5/2/2019.
 */
public class SearchProviderManager {

    private static SearchProviderManager INSTANCE = null;

    private SearchProviderManager() {

    }

    public static synchronized SearchProviderManager getInstance() {
        if (INSTANCE == null) {

            if (INSTANCE == null)
                INSTANCE = new SearchProviderManager();


        }
        return INSTANCE;
    }

    public final SearchProvider searchProvider(Context context) {
        return SearchProviderGenerator.INSTANCE.getSearchProvider(context, REUtils.getTBTAuthHeaders());
    }

}
