package com.plant.toyapp.async;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.WorkerThread;

public abstract class AbsHandlerTask<P,R> implements Runnable {

  private static Handler mUIHandler = new Handler(Looper.getMainLooper());

  private P p;
  private ICallback<R> c;
  private boolean onUi = false;

  public AbsHandlerTask() {
    this(null,null);
  }

  public AbsHandlerTask(P p) {
    this(p,null);
  }

  public AbsHandlerTask(ICallback<R> c) {
    this(null,c);
  }

  public AbsHandlerTask(P p, ICallback<R> c) {
    this.p = p;
    this.c = c;
  }

  @Override public void run() {
    final R r = working(this.p);
    if (this.c != null){
      if (isCallbackOnUIThread()){
        workingOnUIThread(new Runnable() {
          @Override public void run() {
            c.callback(r);
          }
        });
      }else {
        c.callback(r);
      }
    }
  }

  @WorkerThread
  public abstract R working(P p);

  void setCallbackOnUIThread(boolean onUi){
    this.onUi = onUi;
  };
  
  private boolean isCallbackOnUIThread(){
    return onUi;
  }

  private void workingOnUIThread(Runnable r){
    mUIHandler.post(r);
  }
}
