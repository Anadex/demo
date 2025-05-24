package com.example.m16_architecture.data

import com.example.m16_architecture.domain.IUsefulActivitiesRepository
import com.example.m16_architecture.entity.UsefulActivity
import javax.inject.Inject


class UsefulActivitiesRepository @Inject constructor(): IUsefulActivitiesRepository {

    override suspend fun getUsefulActivity(): UsefulActivity {
        return try {
            RetrofitInstance.boredApi.getUsefulActivity()
        } catch (e: Exception) {
            UsefulActivityDto(
                """Не удалось выполнить запрос по причине: 
                    |${e.message}. 
                    |
                    |Попробуйте еще раз.""".trimMargin()
            )
        }
    }

}