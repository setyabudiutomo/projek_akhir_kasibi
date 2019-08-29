package com.navigasi.budi.kasibi.Splash_Screen;

import android.app.Application;
import android.os.SystemClock;

public class Delay extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(3000);
    }
}
