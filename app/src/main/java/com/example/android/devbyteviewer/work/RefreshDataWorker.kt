package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException
import timber.log.Timber


/**
Worker
This class is where you define the actual work (the task) to run in the background. You extend this class
and override the doWork() method. The doWork() method is where you put code to be performed in the background,
such as syncing data with the server or processing images. You implement the Worker in this task.

WorkRequest
This class represents a request to run the worker in background. Use WorkRequest to configure how and when
to run the worker task, with the help of Constraints such as device plugged in or Wi-Fi connected. You
implement the WorkRequest in a later task.

WorkManager
This class schedules and runs your WorkRequest. WorkManager schedules work requests in a way that spreads
out the load on system resources, while honoring the constraints that you specify. You implement the
WorkManager in a later task.

 * */

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
        CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.example.android.devbyteviewer.work.RefreshDataWorker"
    }
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = VideosRepository(database)

        try {
            repository.refreshVideos( )
            Timber.d("WorkManager: Work request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }
        /**
         * Result.success()—work completed successfully.
         * Result.failure()—work completed with a permanent failure.
         * Result.retry()—work encountered a transient failure and should be retried.
         * */

        return Result.success()
    }
}