package com.example.retrofit.models

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import com.example.retrofit.MainActivity
import com.example.retrofit.R

class Animation(
    private val view: MainActivity,
    color1: Int = view.getColor(R.color.grey_blue_transparent),
    color2: Int = view.getColor(R.color.grey_blue)
) {
    private val listOfAnimatedViews = listOf(
        view.binding.image,
        view.binding.name,
        view.binding.gender,
        view.binding.location,
        view.binding.email,
        view.binding.age,
        view.binding.phone
    )

    private val animatorSet = AnimatorSet()

    init {
        val listOfObjectsAnimator = mutableListOf<ObjectAnimator>()

        listOfAnimatedViews.forEach {
            listOfObjectsAnimator.add(
                ObjectAnimator.ofArgb(it, "backgroundColor", color1, color2)
                    .apply {
                        duration = 1000
                        repeatMode = ValueAnimator.REVERSE
                        repeatCount = -1
                    })
        }

        animatorSet.playTogether(listOfObjectsAnimator.toList())
    }

    fun startLoadingAnim() {
        animatorSet.start()
    }

    fun stopLoadingAnim() {
        animatorSet.cancel()

        for (view in listOfAnimatedViews) {
            view.background = null
        }
    }
}