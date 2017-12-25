package com.plant.toyapp;

import android.app.Application;

public class ToyApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();

    registerActivityLifecycleCallbacks(new LoginChecker());
  }
}
