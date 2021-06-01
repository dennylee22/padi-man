package com.bangkit.capstone.padiman.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.capstone.padiman.BuildConfig
import com.bangkit.capstone.padiman.model.Diseases
import com.bangkit.capstone.padiman.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class DiseaseListViewModel: ViewModel(){

    val listDiseases = MutableLiveData<ArrayList<Diseases>>()
    val navigateToDetails = MutableLiveData<Event<String>>()

    fun getList() : LiveData<ArrayList<Diseases>>{
        val listDisease = ArrayList<Diseases>()

        val urlList = "http://34.126.165.65/list"
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", BuildConfig.LIST_TOKEN)
        asyncClient.addHeader("List-Agent", "request")

        asyncClient.get(urlList, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try{
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)


                        val items = responseObject.getJSONArray("penyakit")
                    for (item in 0 until items.length()){
                        val list = Diseases()

                        list.id = item+1
                        list.penyakit = items.getString(item)
                        listDisease.add(list)

                        Log.d("itemList", listDisease.toString())
                    }


                    listDiseases.postValue(listDisease)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : ${R.string.error_1}"
                    403 -> "$statusCode : ${R.string.error_2}"
                    404 -> "$statusCode : ${R.string.error_3}"
                    else -> "$statusCode : ${error.message}"
                }
                navigateToDetails.value = Event(errorMessage)
            }
        })
        return listDiseases
    }



}