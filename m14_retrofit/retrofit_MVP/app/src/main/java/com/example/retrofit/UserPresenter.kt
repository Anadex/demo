package com.example.retrofit

import com.bumptech.glide.Glide
import com.example.retrofit.models.Animation
import com.example.retrofit.models.CurrentUser
import com.example.retrofit.models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object UserPresenter {
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private var view: MainActivity? = null
    private lateinit var loadingAnim: Animation

    fun attachView(activity: MainActivity) {
        view = activity
        loadingAnim = Animation(view!!)
    }

    fun detachView() {
        view = null
    }

    fun startingView() {
        setUser()
        loadUser()
    }

    fun loadCurrentUser() {
        if (UserModel.downloadIsFinished) {
            view!!.binding.button.isEnabled = true
            setUser()
        } else {
            view!!.binding.button.isEnabled = false
            loadingAnim.startLoadingAnim()
            setUser()
        }
    }

    fun refreshUser() {
        loadUser()
    }

    private fun loadUser() {
        coroutineScope.launch {
            view!!.binding.button.isEnabled = false
            loadingAnim.startLoadingAnim()
            UserModel.loadUser()
            setUser()
            loadingAnim.stopLoadingAnim()
            view!!.binding.button.isEnabled = true
        }
    }

    private fun setUser() {
        view!!.binding.name.text =
            if (CurrentUser.name != null) CurrentUser.name else view!!.getText(R.string.failedToLoad)

        view!!.binding.gender.text =
            if (CurrentUser.gender != null) CurrentUser.gender else view!!.getText(R.string.failedToLoad)

        view!!.binding.location.text =
            if (CurrentUser.location != null) CurrentUser.location else view!!.getText(R.string.failedToLoad)

        view!!.binding.email.text =
            if (CurrentUser.email != null) CurrentUser.email else view!!.getText(R.string.failedToLoad)

        view!!.binding.age.text =
            if (CurrentUser.age != null) CurrentUser.age else view!!.getText(R.string.failedToLoad)

        view!!.binding.phone.text =
            if (CurrentUser.phone != null) CurrentUser.phone else view!!.getText(R.string.failedToLoad)

        if (CurrentUser.picture != null) {
            Glide.with(view!!.applicationContext)
                .load(CurrentUser.picture).into(view!!.binding.image)
        } else {
            Glide.with(view!!.applicationContext)
                .load(R.drawable.ic_launcher_foreground).into(view!!.binding.image)
        }
    }

}