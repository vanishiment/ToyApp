package com.plant.toyapp.async;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.Process;
import android.os.SystemClock;
import java.lang.reflect.Field;

public abstract class AbsBaseModel implements IExecuteable{

  private HandlerThread mHandlerThread;
  private Handler mHandler;
  private MessageQueue mQueue;

  public AbsBaseModel() {
    if (mHandlerThread == null){
      mHandlerThread = new HandlerThread("background-thread", Process.THREAD_PRIORITY_BACKGROUND);
      mHandlerThread.start();
    }
    if (null == mHandler){
      mHandler = new Handler(mHandlerThread.getLooper());
    }

    try {
      Field field = Looper.class.getDeclaredField("mQueue");
      field.setAccessible(true);
      mQueue = (MessageQueue) field.get(mHandler.getLooper());
      mQueue.addIdleHandler(new MessageQueue.IdleHandler() {
        @Override public boolean queueIdle() {
          onQueueIdle();
          return true;
        }
      });
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public abstract void onQueueIdle();

  @Override public void execute(AbsHandlerTask r) {
    executeDelay(r,0L);
  }

  @Override public void execute(AbsHandlerTask r, boolean isAsync) {
    executeDelay(r,0L,isAsync);
  }

  @Override public void executeDelay(AbsHandlerTask r, long delayMills) {
    executeDelay(r,delayMills,false);
  }

  @Override
  public void executeDelay(AbsHandlerTask r, long delayMills, boolean isAsync) {
    if (r == null) throw new IllegalArgumentException("execute task can't null.");
    if (delayMills < 0) throw new IllegalArgumentException("delayMills can't < 0.");
    if (isAsync) r.setCallbackOnUIThread(isAsync);
    mHandler.postAtTime(r, r.getToken(),SystemClock.uptimeMillis() + delayMills);
  }

  @Override public void executeDelayWithTimeOut(AbsHandlerTask r, long delayMills, boolean isAsync,
      long timeOut) {
    if (r == null) throw new IllegalArgumentException("execute task can't null.");
    if (delayMills < 0) throw new IllegalArgumentException("delayMills can't < 0.");
    if (isAsync) r.setCallbackOnUIThread(isAsync);
    r.setTimeOut(timeOut);
    mHandler.postAtTime(r, r.getToken(),SystemClock.uptimeMillis() + delayMills);
  }

  @Override public void cancel(Object token) {
    if (token == null) throw new IllegalArgumentException("execute task token can't null.");
    mHandler.removeCallbacksAndMessages(token);
  }

  @Override public void cancelAll() {
    mHandler.removeCallbacksAndMessages(null);
  }
}
