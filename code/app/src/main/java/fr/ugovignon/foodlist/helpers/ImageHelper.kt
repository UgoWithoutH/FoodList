package fr.ugovignon.foodlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch


fun downloadImage(url: String,httpClient: OkHttpClient) : Bitmap?{
    var bitmap: Bitmap? = null
    val request = Request.Builder()
        .url(url)
        .build()
    val countDownLatch = CountDownLatch(1)
    httpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            countDownLatch.countDown();
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                bitmap = BitmapFactory.decodeStream(response.body!!.byteStream())
                countDownLatch.countDown()
            }
        }
    })

    countDownLatch.await()
    return bitmap
}