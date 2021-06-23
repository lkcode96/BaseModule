package com.lk.baselibrary.utils

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * @author: Administrator
 * @date: 2021/5/26
 */
object CacheDataManager {
    /**
     * 获取整体缓存大小
     * @param context
     * @return
     * @throws Exception
     */
    fun getTotalCacheSize(context: Context): String {
        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            context.externalCacheDir?.let {
                cacheSize += getFolderSize(it)
            }
        }
        return getFormatSize(cacheSize)
    }

    /**
     * 获取文件
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     * @param file
     * @return
     * @throws Exception
     */
    fun getFolderSize(file: File): Long {
        var size = 0L
        val fileList = file.listFiles()
        fileList?.forEach {
            size += if (it.isDirectory) {
                getFolderSize(it)
            } else {
                it.length()
            }
        }
        return size
    }

    /**
     * 格式化单位
     * @param size
     */
    fun getFormatSize(size: Long): String {
        val kb = size / 1024
        val m = (kb / 1024)
        val kbs = (kb % 1024)
        return "$m." + kbs + "M"
    }

    /**
     * 清空方法
     * @param context
     */
    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            context.externalCacheDir?.let {
                deleteDir(it)
            }

        }
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            children?.forEach {
                val success = deleteDir(File(dir, it))
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete()
    }
}