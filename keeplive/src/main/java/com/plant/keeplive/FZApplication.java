package com.plant.keeplive;

import android.app.Activity;
import android.app.Application;

public class FZApplication extends Application {

  private static Application application;
  public static Activity mLiveActivity;

  @Override public void onCreate() {
    super.onCreate();
    application = this;
  }

  public static Application self(){
    return application;
  }

  public Activity getLiveActivity() {
    return mLiveActivity;
  }

  public void setLiveActivity(Activity mLiveActivity) {
    this.mLiveActivity = mLiveActivity;
  }
}
