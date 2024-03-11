package com.royalenfield.mapboxNavigation;

import android.content.Context;
//import com.bosch.softtec.components.midgard.navigation.MapboxNavigationProvider;
//import com.bosch.softtec.components.midgard.navigation.NavigationProvider;
import com.bosch.softtec.components.midgard.core.navigation.NavigationProvider;
import com.bosch.softtec.components.midgard.navigation.mapbox.MapboxNavigationProvider;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/**
 * @author BOP1KOR on 5/16/2019.
 */
public final class NavigationProviderGenerator {
    public static final NavigationProviderGenerator INSTANCE;

    @NotNull
    public final NavigationProvider getNavigationProvider(@NotNull Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return (new MapboxNavigationProvider(context));
    }

    static {
        NavigationProviderGenerator navigationProviderGenerator = new NavigationProviderGenerator();
        INSTANCE = navigationProviderGenerator;
    }
}