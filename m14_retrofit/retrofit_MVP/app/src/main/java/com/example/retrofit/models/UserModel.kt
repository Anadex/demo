package com.example.retrofit.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

object UserModel {

    var downloadIsFinished = false

    suspend fun loadUser() {
        downloadIsFinished = false

        val user = withTimeoutOrNull(3000) {// Для имитации длительной и, иногда, неудачной загрузки
            withContext(Dispatchers.IO) {
                delay(2600)
                RetrofitInstance.userApi.getUser().execute().body()
            }
        }

        downloadIsFinished = true

        if (user != null) {
            CurrentUser.name =
                "${user.results[0].name.title} ${user.results[0].name.first} ${user.results[0].name.last}"
            CurrentUser.gender = user.results[0].gender
            CurrentUser.location = user.results[0].location.country
            CurrentUser.email = user.results[0].email
            CurrentUser.age = user.results[0].dob.age
            CurrentUser.phone = user.results[0].phone
            CurrentUser.picture = user.results[0].picture.large
        } else {
            CurrentUser.name = null
            CurrentUser.gender = null
            CurrentUser.location = null
            CurrentUser.email = null
            CurrentUser.age = null
            CurrentUser.phone = null
            CurrentUser.picture = null
        }
    }
}