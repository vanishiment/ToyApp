// IJobCallback.aidl
package com.android.job;

interface IJobCallback {
void acknowledgeStartMessage(int jobId, boolean ongoing);
void acknowledgeStopMessage(int jobId, boolean reschedule);
void jobFinished(int jobId, boolean reschedule);
}
