package fr.ugovignon.foodlist.helpers

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
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

fun getStringFromBitmap(bitmapPicture: Bitmap?): String {
    val compressionQuality = 100
    val encodedImage: String
    val byteArrayBitmapStream = ByteArrayOutputStream()
    bitmapPicture?.compress(
        Bitmap.CompressFormat.PNG, compressionQuality,
        byteArrayBitmapStream
    )
    val b: ByteArray = byteArrayBitmapStream.toByteArray()
    encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
    return encodedImage
}

fun getBitmapFromString(stringPicture: String): Bitmap? {
    val decodedString = Base64.decode(stringPicture, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}