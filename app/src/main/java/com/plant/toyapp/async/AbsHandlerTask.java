package com.plant.toyapp.async;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.WorkerThread;

public abstract class AbsHandlerTask<P, R> implements Runnable {

  private static Handler mUIHandler = new Handler(Looper.getMainLooper());

  private P p;
  private ICallback<R> c;
  private boolean onUi = false;
  private long timeOut = 0L;
  private volatile boolean isTimeOut = false;
  private Object token;

  public AbsHandlerTask() {
    this(null, null);
  }

  public AbsHandlerTask(P p) {
    this(p, null);
  }

  public AbsHandlerTask(ICallback<R> c) {
    this(null, c);
  }

  public AbsHandlerTask(P p, ICallback<R> c) {
    this.p = p;
    this.c = c;
  }

  @Override public void run() {
    if (timeOut > 0) beginTimeOut();
    token = getToken();
    final R r = working(this.p, isTimeOut, c);
    if (this.c != null) {
      if (isCallbackOnUIThread()) {
        workingOnUIThread(new Runnable() {
          @Override public void run() {
            c.callback(token,r);
          }
        });
      } else {
        c.callback(token,r);
      }
    }
  }

  @WorkerThread public abstract R working(P p, boolean isTimeOut, ICallback<R> c);

  void setCallbackOnUIThread(boolean onUi) {
    this.onUi = onUi;
  }

  void setTimeOut(long timeOut) {
    this.timeOut = timeOut;
  }

  private boolean isCallbackOnUIThread() {
    return onUi;
  }

  private void workingOnUIThread(Runnable r) {
    mUIHandler.post(r);
  }

  private void beginTimeOut() {
    mUIHandler.postDelayed(new Runnable() {
      @Override public void run() {
        isTimeOut = true;
      }
    }, timeOut);
  }

  public abstract Object getToken();

}
