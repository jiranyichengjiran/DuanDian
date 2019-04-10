package movie.bw.com.duandian

import android.app.Application

class MyApplication :Application() {
    override fun onCreate() {
        super.onCreate()
    }
    companion object {
        var downloadId=0L  //下载任务的ID号
    }
}