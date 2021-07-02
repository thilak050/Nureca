package com.example.nureca.ui.main.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nureca.data.model.StateList
import com.example.nureca.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    val stateList = MutableLiveData<List<StateList>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllMovies() {

        val response = repository.getAllStates()
        response.enqueue(object : Callback<List<StateList>> {
            override fun onResponse(call: Call<List<StateList>>, response: Response<List<StateList>>) {
                stateList.postValue(response.body())
            }

            override fun onFailure(call: Call<List<StateList>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}