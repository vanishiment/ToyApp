package com.plant.keeplive;

import android.app.Service;
import android.app.job.JobParameters;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class KeepLiveService extends JobService {

  private static final String TAG = "KeepLiveService";

  private volatile static Service mLiveService = null;

  public static boolean isServiceLive() {
    return mLiveService != null;
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    mLiveService = this;
    Log.e(TAG, "onStartCommand " );
    return Service.START_STICKY;
  }

  @Override public boolean onStartJob(JobParameters params) {
    return false;
  }

  @Override public boolean onStopJob(JobParameters params) {
    return false;
  }

  public static class InnerService extends Service {

    @Override public IBinder onBind(Intent intent) {
      return null;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
      KeepLiveManager.get().setForeground(mLiveService, this);
      return super.onStartCommand(intent, flags, startId);
    }
  }
}
