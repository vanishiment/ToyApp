package com.plant.toyapp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class LoginChecker implements Application.ActivityLifecycleCallbacks {

  private static final List<Class<? extends Activity>> IGNORE_ACTIVITIES = new ArrayList<>();

  private Intent mCacheIntent;

  static {
    IGNORE_ACTIVITIES.add(SplashActivity.class);
    IGNORE_ACTIVITIES.add(ActiveActivity.class);
  }

  private boolean isIgnored(Activity activity){
    return IGNORE_ACTIVITIES.contains(activity.getClass());
  }

  private boolean hasLogin(){
    return true;
  }

  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    if (!hasLogin() && !isIgnored(activity)){
      mCacheIntent = activity.getIntent();
      LoginActivity.launch(activity);
      activity.finish();
    }
  }

  @Override public void onActivityStarted(Activity activity) {

  }

  @Override public void onActivityResumed(Activity activity) {

  }

  @Override public void onActivityPaused(Activity activity) {
    if (activity instanceof LoginActivity && hasLogin()){
      if (mCacheIntent != null){
        activity.startActivity(mCacheIntent);
        mCacheIntent = null;
      }else {
        activity.startActivity(new Intent(activity,MainActivity.class));
      }
    }
  }

  @Override public void onActivityStopped(Activity activity) {

  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

  }

  @Override public void onActivityDestroyed(Activity activity) {

  }
}
