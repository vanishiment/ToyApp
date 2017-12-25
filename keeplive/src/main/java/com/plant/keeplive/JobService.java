package com.plant.keeplive;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobServiceEngine;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.android.job.IJobCallback;
import com.android.job.IJobService;
import java.lang.reflect.Field;

public abstract class JobService extends Service {

  private static final String TAG = "JobService";

  public static final String PERMISSION_BIND =
      "android.permission.BIND_JOB_SERVICE";

  private Object mEngine;

  private final int MSG_EXECUTE_JOB = 0;
  private final int MSG_STOP_JOB = 1;
  private final int MSG_JOB_FINISHED = 2;

  private final Object mHandlerLock = new Object();

  JobHandler mHandler;

  IInterface mBinder;

  public JobService() {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
      mBinder = new IJobService.Stub() {
        @Override
        public void startJob(JobParameters jobParams) {
          ensureHandler();
          Message m = Message.obtain(mHandler, MSG_EXECUTE_JOB, jobParams);
          m.sendToTarget();
        }
        @Override
        public void stopJob(JobParameters jobParams) {
          ensureHandler();
          Message m = Message.obtain(mHandler, MSG_STOP_JOB, jobParams);
          m.sendToTarget();
        }
      };
    }

  }

  ///** Binder for this service. */
  //IJobService mBinder = new IJobService.Stub() {
  //  @Override
  //  public void startJob(JobParameters jobParams) {
  //    ensureHandler();
  //    Message m = Message.obtain(mHandler, MSG_EXECUTE_JOB, jobParams);
  //    m.sendToTarget();
  //  }
  //  @Override
  //  public void stopJob(JobParameters jobParams) {
  //    ensureHandler();
  //    Message m = Message.obtain(mHandler, MSG_STOP_JOB, jobParams);
  //    m.sendToTarget();
  //  }
  //};

  void ensureHandler() {
    synchronized (mHandlerLock) {
      if (mHandler == null) {
        mHandler = new JobHandler(getMainLooper());
      }
    }
  }

  class JobHandler extends Handler {
    JobHandler(Looper looper) {
      super(looper);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) @Override
    public void handleMessage(Message msg) {
      final JobParameters params = (JobParameters) msg.obj;
      switch (msg.what) {
        case MSG_EXECUTE_JOB:
          try {
            boolean workOngoing = JobService.this.onStartJob(params);
            ackStartMessage(params, workOngoing);
          } catch (Exception e) {
            Log.e(TAG, "Error while executing job: " + params.getJobId());
            throw new RuntimeException(e);
          }
          break;
        case MSG_STOP_JOB:
          try {
            boolean ret = JobService.this.onStopJob(params);
            ackStopMessage(params, ret);
          } catch (Exception e) {
            Log.e(TAG, "Application unable to handle onStopJob.", e);
            throw new RuntimeException(e);
          }
          break;
        case MSG_JOB_FINISHED:
          final boolean needsReschedule = (msg.arg2 == 1);
          IJobCallback callback = getIJobCallback(params);
          if (callback != null) {
            try {
              callback.jobFinished(params.getJobId(), needsReschedule);
            } catch (RemoteException e) {
              Log.e(TAG, "Error reporting job finish to system: binder has gone" +
                  "away.");
            }
          } else {
            Log.e(TAG, "finishJob() called for a nonexistent job id.");
          }
          break;
        default:
          Log.e(TAG, "Unrecognised message received.");
          break;
      }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ackStartMessage(JobParameters params, boolean workOngoing) {
      final IJobCallback callback = getIJobCallback(params);
      final int jobId = params.getJobId();
      if (callback != null) {
        try {
          callback.acknowledgeStartMessage(jobId, workOngoing);
        } catch(RemoteException e) {
          Log.e(TAG, "System unreachable for starting job.");
        }
      } else {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
          Log.d(TAG, "Attempting to ack a job that has already been processed.");
        }
      }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ackStopMessage(JobParameters params, boolean reschedule) {
      final IJobCallback callback = getIJobCallback(params);
      final int jobId = params.getJobId();
      if (callback != null) {
        try {
          callback.acknowledgeStopMessage(jobId, reschedule);
        } catch(RemoteException e) {
          Log.e(TAG, "System unreachable for stopping job.");
        }
      } else {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
          Log.d(TAG, "Attempting to ack a job that has already been processed.");
        }
      }
    }
  }

  public final IBinder onBind(Intent intent) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
      return null;
    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
      if (mEngine == null) {
        mEngine = new JobServiceEngine(this) {
          @Override
          public boolean onStartJob(JobParameters params) {
            return com.plant.keeplive.JobService.this.onStartJob(params);
          }

          @Override
          public boolean onStopJob(JobParameters params) {
            return com.plant.keeplive.JobService.this.onStopJob(params);
          }
        };
      }
      JobServiceEngine engine = (JobServiceEngine) mEngine;
      return engine.getBinder();
    }else {
      return mBinder.asBinder();
    }
  }

  public abstract boolean onStartJob(JobParameters params);

  public abstract boolean onStopJob(JobParameters params);

  public final void jobFinished(JobParameters params, boolean needsReschedule) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
      if (mEngine != null){
        JobServiceEngine engine = (JobServiceEngine) mEngine;
        engine.jobFinished(params, needsReschedule);
      }
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
      ensureHandler();
      Message m = Message.obtain(mHandler, MSG_JOB_FINISHED, params);
      m.arg2 = needsReschedule ? 1 : 0;
      m.sendToTarget();
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private IJobCallback getIJobCallback(JobParameters params){
    try {
      Field field = JobParameters.class.getDeclaredField("callback");
      field.setAccessible(true);
      IBinder iBinder = (IBinder) field.get(params);
      return IJobCallback.Stub.asInterface(iBinder);
    }catch (Exception e){
      return null;
    }
  }
}
