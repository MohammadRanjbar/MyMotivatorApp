package com.example.mymotivator.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.mymotivator.R
import dagger.hilt.android.qualifiers.ApplicationContext

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class StorageAndShareHelper  constructor(  context: Activity) {


    private val activity = context

    var app_installed = false


    fun saveOrShareImage(id: Int): ResponseOfStorage {


        var savedImagePath: String?
        var imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg"

        val bitmap = getBitMap(R.id.image_container)


        savedImagePath = insertImageIntoExternalFile(
            activity.contentResolver!!,
            bitmap,
            "MyMotivatePhotos ${System.currentTimeMillis()}",
            "nothing"
        )

        if (savedImagePath != null) {
            if (id == R.id.save) {
                return ResponseOfStorage.savedInGalley
            } else if (id == R.id.upload_img) {
                return shareToInstagram(savedImagePath)

            }
        } else
            return ResponseOfStorage.errorOccured
        //  Log.i(Config.APP_LOG_TAG, "saveImage: $savedImagePath ")

        return ResponseOfStorage.errorOccured
    }


    private fun insertImageIntoExternalFile(
        cr: ContentResolver,
        source: Bitmap?,
        title: String?,
        description: String?
    ): String? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        // values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        var url: Uri? = null
        var stringUrl: String? = null
        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (source != null) {
                val imageOut = cr.openOutputStream(url!!)
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut)
                } finally {
                    imageOut!!.close()
                }

            } else {
                if (url != null)
                    cr.delete(url, null, null)


            }
        } catch (e: java.lang.Exception) {

            if (url != null)
                cr.delete(url, null, null)

        }
        if (url != null) {
            stringUrl = url.toString();
        }
        return stringUrl

    }

    private fun getBitMap(shareImgContainer: Int): Bitmap {
        val view = activity.findViewById<View>(shareImgContainer)
        val returnBitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnBitmap)

        val drawable = view.background

        if (drawable != null) {
            drawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnBitmap

    }

    private fun galleryAddPic(savedImagePath: String?): ResponseOfStorage {
        savedImagePath?.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            activity.sendBroadcast(mediaScanIntent)
            return ResponseOfStorage.savedInGalley
        }
        return ResponseOfStorage.errorOccured
    }

    private fun shareToInstagram(savedImagePath: String): ResponseOfStorage {

        try {
            val info = activity.packageManager.getApplicationInfo("com.instagram.android", 0)

        } catch (e: java.lang.Exception) {
            return ResponseOfStorage.instagramNotInstall
        }

        val uri = Uri.parse(savedImagePath)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.setPackage("com.instagram.android")
        activity.startActivity(shareIntent)
        // Log.i(Config.APP_LOG_TAG, "shareToInstagram:${uri} ")
        return ResponseOfStorage.sendToInstagram


    }

}