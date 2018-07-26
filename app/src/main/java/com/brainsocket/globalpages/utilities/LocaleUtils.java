package com.brainsocket.globalpages.utilities;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.view.ContextThemeWrapper;

import java.util.ArrayList;
import java.util.Locale;


public class LocaleUtils {

    private static Locale sLocale;

    public static void setLocale(Locale locale) {
        sLocale = locale;
        if(sLocale != null) {
            Locale.setDefault(sLocale);
        }
    }

    public static void updateConfig(ContextThemeWrapper wrapper) {
        if(sLocale != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = new Configuration();
            configuration.setLocale(sLocale);
            wrapper.applyOverrideConfiguration(configuration);
        }
    }

    public static void updateConfig(Application app, Configuration configuration) {
        if (sLocale != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //Wrapping the configuration to avoid Activity endless loop
            Configuration config = new Configuration(configuration);
            // We must use the now-deprecated config.locale and res.updateConfiguration here,
            // because the replacements aren't available till API level 24 and 17 respectively.
            config.locale = sLocale;
            Resources res = app.getBaseContext().getResources();
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
    }

    ///Language stuff
    public interface LanguageListener
    {
        public void onLanguageChange();
    }

    static ArrayList<LanguageListener> listeners = new ArrayList<>();

    public static void registerLangListener(LanguageListener listener)
    {
        listeners.add(listener);
    }

    public static void removeListener(LanguageListener listener)
    {
        if(listeners.contains(listener))
            listeners.remove(listener);
    }

    public static void broadcastLanguageChange()
    {
        for ( LanguageListener listener : listeners)
            listener.onLanguageChange();
    }
}
