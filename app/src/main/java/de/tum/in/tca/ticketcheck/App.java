package de.tum.in.tca.ticketcheck;

import android.app.Application;
import android.os.StrictMode;

import net.danlew.android.joda.JodaTimeAndroid;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupStrictMode();
        JodaTimeAndroid.init(this);
    }

    protected void setupStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                               .detectAll()
                                               .permitDiskReads()  // those is mainly caused by shared preferences and room. probably enable
                                               .permitDiskWrites() // this as soon as we don't call allowMainThreadQueries() in TcaDb
                                               .penaltyLog()
                                               .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                                           .detectActivityLeaks()
                                           //.detectLeakedClosableObjects() // seems like room / DAOs leak
                                           .detectLeakedRegistrationObjects()
                                           .detectFileUriExposure()
                                           //.detectCleartextNetwork() // not available at the current minSdk
                                           //.detectContentUriWithoutPermission()
                                           //.detectUntaggedSockets()
                                           .penaltyLog()
                                           .build());
        }
    }
}
