package com.example.tikitrendingproject.view

import android.view.View

interface Action<T> {
    fun onClick(t: T)
    fun onClickWithBackground(view: View, t: T)
}