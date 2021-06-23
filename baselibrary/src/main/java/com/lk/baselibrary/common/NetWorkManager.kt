package com.lk.baselibrary.common

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lk.baselibrary.utils.convert.MyConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object NetWorkManager {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(getClient())
            .baseUrl(Constant.BaseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MyConverterFactory.create())
            .build()
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5000L, TimeUnit.MILLISECONDS)
            .readTimeout(10_000, TimeUnit.MILLISECONDS)
            .writeTimeout(30_000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

//    private fun mTokenInterceptor(chain: Interceptor.Chain): Response {
//        //ShowLog.e(SpUtils.getString("token"))
//        ShowLog.e(chain.request().toString())
////        val request = chain.request().newBuilder().addHeader(
////            "Authorization",
////            SpUtils.getString("token")
////        ).build()
//        return chain.proceed(request)
//    }

}