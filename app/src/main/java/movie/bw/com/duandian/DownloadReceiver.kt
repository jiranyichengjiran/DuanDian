package movie.bw.com.duandian

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent!!.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (MyApplication.downloadId == id) {
                installApk(context, id)
                MyApplication.downloadId = 0L  //重置
            }
        } else if (intent.action == DownloadManager.ACTION_NOTIFICATION_CLICKED) {
            val viewDownloadIntent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
            viewDownloadIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(viewDownloadIntent)
        }
    }

    private fun installApk(context: Context, downloadApkId: Long) {

        val dManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val install = Intent(Intent.ACTION_VIEW)
        val downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId)
        if (downloadFileUri != null) {
            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive")
            if (Build.VERSION.SDK_INT >= 24) {  //判读版本是否在7.0以上
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)  //对目标应用临时授权该uri所代表的的文件
            }
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (install.resolveActivity(context.packageManager) != null) {
                context.startActivity(install)
            } else {
                Log.e("installApk", "自动安装失败,请手动安装")
            }
        } else {
            Log.e("DownloadManager", "download error")
        }

    }
}