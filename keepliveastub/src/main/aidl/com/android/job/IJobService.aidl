// IJobService.aidl
package com.android.job;

interface IJobService {
    /** Begin execution of application's job. */
        void startJob(in JobParameters jobParams);
        /** Stop execution of application's job. */
        void stopJob(in JobParameters jobParams);
}
