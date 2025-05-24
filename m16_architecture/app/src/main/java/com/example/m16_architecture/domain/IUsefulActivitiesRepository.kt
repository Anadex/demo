package com.example.m16_architecture.domain

import com.example.m16_architecture.entity.UsefulActivity

interface IUsefulActivitiesRepository {
    suspend fun getUsefulActivity(): UsefulActivity
}