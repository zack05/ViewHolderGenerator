package com.fairy.templateapp.utils

import com.fairy.templateapp.models.Todo
import retrofit2.http.GET
import retrofit2.http.Path

interface Webservice {
    @GET("/todos/{id}")
    suspend fun getTodo(@Path(value = "id") todoId: Int): Todo
}