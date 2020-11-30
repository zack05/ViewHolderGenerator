package com.fairy.templateapp

import com.fairy.templateapp.utils.RetrofitClient
import com.fairy.templateapp.utils.Webservice

class TodoRepository {

    var client: Webservice = RetrofitClient.webservice

    suspend fun getTodo(id: Int) = client.getTodo(id)
}