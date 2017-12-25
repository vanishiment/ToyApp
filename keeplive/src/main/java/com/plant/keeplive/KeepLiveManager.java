package com.plant.keeplive;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

/**
 * 提升优先级，防止 kill
 * 1. 1 像素 Activity 提权           全部
 * 2. Notification 提权             全部
 * 进程死后拉活
 * 3. 系统广播                       3、4 禁止自启无法接收到广播
 * 4. 第三方应用广播
 * 5. 利用系统 Service 机制拉活       第一次 5s 重启，第二次 10s 重启，第三次 20s 重启，短时间内达到 5 次，无法重启；fore stop 无法重启
 * 6. Native 机制拉活                Android 5.0 之下有效
 * 7. JobScheduler 机制拉活          Android 5.0 之后有效
 * 8. 第三方 Push 拉活
 */
public class KeepLiveManager {

  private static final String TAG = "KeepLiveManager";

  private static class Holder{
    static final KeepLiveManager KEEP_LIVE_MANAGER = new KeepLiveManager();
  }

  public static KeepLiveManager get(){
    return Holder.KEEP_LIVE_MANAGER;
  }

  public void startKeepLiveActivity(){
    if (FZApplication.mLiveActivity != null) return;
    FZApplication.self().startActivity(new Intent(FZApplication.self(),KeepLiveActivity.class));
  }

  public void finishKeepLiveActivity(){
    FZApplication.mLiveActivity.finish();
    FZApplication.mLiveActivity = null;
  }

  public void startServiceLive(){
    if (KeepLiveService.isServiceLive()) return;
    FZApplication.self().startService(new Intent(FZApplication.self(),KeepLiveService.class));
  }

  public void setForeground(Service keepLiveService,Service innerService){
    int foregroundPushId = 1;
    if (keepLiveService != null){
      if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
        keepLiveService.startForeground(foregroundPushId,new Notification());
      }else {
        keepLiveService.startForeground(foregroundPushId,new Notification());
        if (innerService != null){
          innerService.startForeground(foregroundPushId,new Notification());
          innerService.stopSelf();
        }
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public void startJobScheduler() {
    Log.e(TAG, "startJobScheduler " );
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;
    try {
      int jobId = 1;
      JobInfo.Builder builder = new JobInfo.Builder(jobId,
          new ComponentName(FZApplication.self(), KeepLiveLollipopService.class));
      builder.setPeriodic(5000);
      builder.setPersisted(true);
      JobScheduler jobScheduler =
          (JobScheduler) FZApplication.self().getSystemService(Context.JOB_SCHEDULER_SERVICE);
      if (jobScheduler != null) {
        jobScheduler.schedule(builder.build());
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }


}
