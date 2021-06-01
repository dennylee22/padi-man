package com.bangkit.capstone.padiman.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.capstone.padiman.BuildConfig
import com.bangkit.capstone.padiman.R
import com.bangkit.capstone.padiman.model.Upload
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.io.IOException

@Suppress("DEPRECATION")
class DetectorActivity : AppCompatActivity() {

    private var btnDetector = false
    private var itemDetector: Upload? = null
    private lateinit var detector: Upload

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detector)

        initialization()

        onClick()

        //fungsi back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onClick() {
        cameraButton.setOnClickListener {
            cameraPermission()
        }

        galleryButton.setOnClickListener {
            galleryPermission()
        }
    }

    private fun galleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), REQUEST_CODE_FOR_GALLERY
                )
            } else {
                galleryIntent()
            }
        } else {
            galleryIntent()
        }
    }

    private fun postPhoto(file: Int){
        val asyncClient = AsyncHttpClient()
        val url = "http://34.126.165.65:9000/upload"
        asyncClient.addHeader("Authorization", BuildConfig.LIST_TOKEN)
        asyncClient.addHeader("List-Agent", "request")

        asyncClient.post(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : ${R.string.error_1}"
                    403 -> "$statusCode : ${R.string.error_2}"
                    404 -> "$statusCode : ${R.string.error_3}"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetectorActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun galleryIntent() {
        val callGalleryIntent = Intent()
        callGalleryIntent.type = "image/*"
        callGalleryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(callGalleryIntent, "Select image"),
            REQUEST_CODE_FOR_GALLERY
        )
    }

    private fun cameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            ) {
                val permission = arrayOf(
                    android.Manifest.permission.CAMERA
                )
                requestPermissions(permission, REQUEST_CODE_FOR_CAMERA)
            } else {
                cameraIntent()
            }
        } else {
            cameraIntent()
        }
    }

    private fun cameraIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(this.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_CODE_FOR_CAMERA)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_FOR_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraIntent()

            } else {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show()
            }

        } else if (requestCode == REQUEST_CODE_FOR_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryIntent()

            } else {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_FOR_CAMERA && resultCode == Activity.RESULT_OK) {
            try {
                bitmap = data!!.extras!!.get("data") as Bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap!!, 500, 500, true)
                imageView.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (requestCode == REQUEST_CODE_FOR_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                val imageUri = data!!.data
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                bitmap = Bitmap.createScaledBitmap(bitmap!!, 500, 500, true)
                imageView.setImageURI(imageUri)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun initialization() {
        imageView = findViewById(R.id.image_upload)
        cameraButton = findViewById(R.id.btn_camera)
        galleryButton = findViewById(R.id.btn_gallery)
    }

    companion object{
        private lateinit var imageView: ImageView
        private lateinit var cameraButton: Button
        private lateinit var galleryButton: Button
        private var REQUEST_CODE_FOR_CAMERA = 1
        private var REQUEST_CODE_FOR_GALLERY = 2
        private var bitmap: Bitmap? = null
    }

    //back
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    //fungsi back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}