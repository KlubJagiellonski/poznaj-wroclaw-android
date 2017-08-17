package pl.poznajapp

import android.app.Application

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.poznajapp.helpers.Utils
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Rafał Gawlik on 11.08.17.
 */

class PoznajApp : Application() {

    internal var retrofit: Retrofit = null!!

    override fun onCreate() {
        super.onCreate()


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .connectTimeout(Utils.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(Utils.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()


        retrofit = Retrofit.Builder()
                .baseUrl(this.resources.getString(R.string.poznaj_app_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()

    }
}