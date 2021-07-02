package com.example.nureca.ui.main.view

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nureca.R
import com.example.nureca.data.api.RetrofitService
import com.example.nureca.data.repository.MainRepository
import com.example.nureca.databinding.ActivityHomePageBinding
import com.example.nureca.ui.base.ViewModelFactory
import com.example.nureca.ui.main.adapter.MainAdapter
import com.example.nureca.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlin.random.Random


class HomePage : AppCompatActivity() {
    private val TAG = "HomePage"
    private lateinit var binding: ActivityHomePageBinding

    lateinit var viewModel: MainViewModel

    private val retrofitService = RetrofitService.getInstance()
    val adapter = MainAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()


        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        viewModel = ViewModelProvider(this, ViewModelFactory(MainRepository(retrofitService))).get(MainViewModel::class.java)

        binding.recyclerview.adapter = adapter
        binding.recyclerview.visibility=View.GONE
        binding.progressbar.visibility=View.VISIBLE

        viewModel.stateList.observe(this, Observer {
            binding.recyclerview.visibility=View.VISIBLE
            binding.progressbar.visibility=View.GONE
            adapter.setMovieList(it)
        })

        viewModel.errorMessage.observe(this, Observer {

        })
        viewModel.getAllMovies()




    }



}