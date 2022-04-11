package main.api

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

class OkHttpRequest {
    fun getRSS(url: String, callback: Callback): Call {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(callback)
        return call
    }
}