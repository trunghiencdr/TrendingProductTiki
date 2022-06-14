package com.example.tikitrendingproject.util

import android.util.Log
import androidx.appcompat.widget.SearchView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


object RxSearchObservable {
    fun fromView(searchView: SearchView): Observable<String> {
        val subject = PublishSubject.create<String>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                subject.onComplete()
                return true
            }
            override fun onQueryTextChange(text: String): Boolean {
                subject.onNext(text)
                writeLogDebug("On query text change $text")
                return true
            }
        })
        return subject
    }
}