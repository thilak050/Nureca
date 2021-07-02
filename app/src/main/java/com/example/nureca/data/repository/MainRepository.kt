package com.example.nureca.data.repository

import com.example.nureca.data.api.RetrofitService


class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getAllStates() = retrofitService.getAllStates()
}