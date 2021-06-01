package com.bangkit.capstone.padiman.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bangkit.capstone.padiman.BuildConfig
import com.bangkit.capstone.padiman.R
import com.bangkit.capstone.padiman.model.Diseases
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    private var itemList: Diseases? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setTitle(R.string.detail)

        val id = intent.getIntExtra(EXTRA_ID,0)
        getDetail(id)

        //fungsi back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun getDetail(id: Int){
        val asynClient = AsyncHttpClient()
        val url = "http://34.126.165.65/list?id=$id"
        asynClient.addHeader("Authorization", BuildConfig.LIST_TOKEN)
        asynClient.addHeader("List-Agent", "request")

        asynClient.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val detail = Diseases()

                    val solusi = responseObject.getJSONArray("solusi")

                    val stringBuilder = StringBuilder()
                    for (i in 0 until solusi.length()){
                        stringBuilder.append("${i+1}. ${solusi.getString(i)}\n")
                    }
                    detail.penyakit = responseObject.getString("penyakit")

                    itemList = detail

                    Log.d("itemDetail", itemList.toString())

                    text_penyakit.text = responseObject.getString("penyakit")
                    text_solusi.text = stringBuilder.toString()
                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : ${R.string.error_1}"
                    403 -> "$statusCode : ${R.string.error_2}"
                    404 -> "$statusCode : ${R.string.error_3}"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val EXTRA_ID = "extra_id"
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