package com.bangkit.capstone.padiman.detector

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.capstone.padiman.R
import com.bangkit.capstone.padiman.desease.util.ApiService
import okhttp3.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.util.concurrent.atomic.AtomicInteger

class DetectorrrActivity : AppCompatActivity() {
    var mBitmap: Bitmap? = null
    var textView: TextView? = null
    var text_solusi: TextView? = null
    var fabCamera: Button? = null
    var fabUpload: Button? = null
    var imageView: ImageView? = null
    private var imageUri: Uri? = null
    var apiService: ApiService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detector)
        fabCamera = findViewById(R.id.fab)
        fabUpload = findViewById(R.id.fabUpload)
        textView = findViewById(R.id.textView)
        text_solusi = findViewById(R.id.textView_solusi)
        imageView = findViewById(R.id.imageView)
        initRetrofitClient()

        onClick()

        //fungsi back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onClick(){
        fabCamera?.setOnClickListener {
            openfilechooser()
        }
        fabUpload?.setOnClickListener {
            multipartImageUpload()
        }
    }

    private fun initRetrofitClient() {
        val client = OkHttpClient.Builder().build()
        apiService = Retrofit.Builder()
            .baseUrl("http://34.126.165.65")
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    private fun openfilechooser() {
        val intentf = Intent()
        intentf.type = "image/*"
        intentf.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intentf, 1)
    }

    private fun multipartImageUpload() {
        try {
            val filesDir = applicationContext.filesDir
            val file = File(filesDir, "image" + ".jpg")
            val bos = ByteArrayOutputStream()
            mBitmap!!.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapdata = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            val reqFile =
                RequestBody.create(MediaType.parse("image/jpg"), file)
            val body =
                MultipartBody.Part.createFormData("file", file.name, reqFile)
            val name =
                RequestBody.create(MediaType.parse("text/plain"), "file")
            val req = apiService!!.postImage(body, name)
            req?.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try {
                        val result = response.body()!!.string()
                        val responseObject = JSONObject(result)
                        Log.d("response", responseObject.toString())
                        val solusi = responseObject.getJSONArray("solusi")
                        val sb = StringBuilder()
                        for (i in 0 until solusi.length()) {
                            sb.append(_tNum.getAndIncrement())
                            sb.append(". ")
                            sb.append(solusi.getString(i)).toString()
                            sb.append("\n")
                        }
                        textView!!.text = responseObject.getString("penyakit")
                        text_solusi!!.text = sb.toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (response.code() == 200) {
                        Log.d("berhasil", response.toString())
                    }
                    Toast.makeText(
                        applicationContext,
                        response.code().toString() + " ",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(
                    call: Call<ResponseBody>,
                    t: Throwable
                ) {
                    Toast.makeText(applicationContext, "Request failed", Toast.LENGTH_SHORT)
                        .show()
                    t.printStackTrace()
                }
            })
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val imageView =
                findViewById<ImageView>(R.id.imageView)
            if (requestCode == 1) {
                imageUri = data!!.data
                //  imageView.setImageURI(imageUri);
                if (imageUri != null) {
                    try {
                        mBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                        imageView.setImageBitmap(mBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    companion object {
        private val _tNum =
            AtomicInteger(1)
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