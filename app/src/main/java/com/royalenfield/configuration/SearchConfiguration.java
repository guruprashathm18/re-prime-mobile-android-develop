package com.royalenfield.configuration;

/**
 * @author BOP1KOR on 5/3/2019.
 */
public final class SearchConfiguration {
    public static final int SEARCH_LIMIT = 40;
    public static final SearchConfiguration INSTANCE;

    static {
        SearchConfiguration var0 = new SearchConfiguration();
        INSTANCE = var0;
    }
}
