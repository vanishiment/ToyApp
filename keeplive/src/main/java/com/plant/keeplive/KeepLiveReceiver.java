package com.plant.keeplive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * 在 AndroidManifest.xml 中注册静态广播，添加系统或者第三方广播事件
 */
public class KeepLiveReceiver extends BroadcastReceiver {

  @Override public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (TextUtils.isEmpty(action)) return;
    if (action.equals(Intent.ACTION_SCREEN_OFF)){
      KeepLiveManager.get().startKeepLiveActivity();
    }else if (action.equals(Intent.ACTION_USER_PRESENT)){
      KeepLiveManager.get().finishKeepLiveActivity();
    }
    KeepLiveManager.get().startServiceLive();
  }
}
