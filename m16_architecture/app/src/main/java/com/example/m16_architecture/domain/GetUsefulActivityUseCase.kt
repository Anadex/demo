package com.example.m16_architecture.domain

import com.example.m16_architecture.entity.UsefulActivity
import javax.inject.Inject

class GetUsefulActivityUseCase @Inject constructor(private val usefulActivitiesRepository: IUsefulActivitiesRepository) {

    suspend fun execute(): UsefulActivity = usefulActivitiesRepository.getUsefulActivity()

}