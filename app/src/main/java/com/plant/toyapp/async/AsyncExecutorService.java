package com.plant.toyapp.async;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class AsyncExecutorService {

  private static final int DEFAULT_THREAD_COUNT = 3;

  private AbsBaseModel[] baseModels;

  private static final class Holder{
    private static final AsyncExecutorService SERVICE = new AsyncExecutorService();
  }
  public static AsyncExecutorService get(){
    return Holder.SERVICE;
  }

  private AsyncExecutorService() {
    init();
  }

  private void init(){

  }

  private void adjustThreadCount(NetworkInfo info) {
    if (info == null || !info.isConnectedOrConnecting()) {
      setThreadCount(DEFAULT_THREAD_COUNT);
      return;
    }
    switch (info.getType()) {
      case ConnectivityManager.TYPE_WIFI:
      case ConnectivityManager.TYPE_WIMAX:
      case ConnectivityManager.TYPE_ETHERNET:
        setThreadCount(4);
        break;
      case ConnectivityManager.TYPE_MOBILE:
        switch (info.getSubtype()) {
          case TelephonyManager.NETWORK_TYPE_LTE:  // 4G
          case TelephonyManager.NETWORK_TYPE_HSPAP:
          case TelephonyManager.NETWORK_TYPE_EHRPD:
            setThreadCount(3);
            break;
          case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
          case TelephonyManager.NETWORK_TYPE_CDMA:
          case TelephonyManager.NETWORK_TYPE_EVDO_0:
          case TelephonyManager.NETWORK_TYPE_EVDO_A:
          case TelephonyManager.NETWORK_TYPE_EVDO_B:
            setThreadCount(2);
            break;
          case TelephonyManager.NETWORK_TYPE_GPRS: // 2G
          case TelephonyManager.NETWORK_TYPE_EDGE:
            setThreadCount(1);
            break;
          default:
            setThreadCount(DEFAULT_THREAD_COUNT);
        }
        break;
      default:
        setThreadCount(DEFAULT_THREAD_COUNT);
    }
  }

  private void setThreadCount(int threadCount){
    baseModels = new AbsBaseModel[threadCount];
    for (int i = 0; i < baseModels.length; i++) {
      baseModels[i] = new AbsBaseModel() {
        @Override public void onQueueIdle() {

        }
      };
    }
  }

}
