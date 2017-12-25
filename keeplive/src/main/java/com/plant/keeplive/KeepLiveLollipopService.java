package com.plant.keeplive;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class KeepLiveLollipopService extends JobService {

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  @Override public boolean onStartJob(JobParameters params) {
    return false;
  }

  @Override public boolean onStopJob(JobParameters params) {
    return false;
  }
}
