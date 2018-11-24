package com.keepcoding.filmica.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

object FilmsRepo {
    private val films: MutableList<Film> = mutableListOf()

    fun findFilmById(id: String): Film? {
        return films.find { film -> film.id == id }
    }

    fun discoverFilms(
        context: Context,
        callbackSuccess: ((MutableList<Film>) -> Unit),
        callbackError: ((VolleyError) -> Unit)
    ) {

        if (films.isEmpty()) {
            requestDiscoverFilms(callbackSuccess, callbackError, context)
        } else {
            callbackSuccess.invoke(films)
        }
    }

    private fun requestDiscoverFilms(
        callbackSuccess: (MutableList<Film>) -> Unit,
        callbackError: (VolleyError) -> Unit,
        context: Context
    ) {
        val url = ApiRoutes.discoverUrl()
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val newFilms = Film.parseFilms(response)
                films.addAll(newFilms)
                callbackSuccess.invoke(films)
            },
            { error ->
                callbackError.invoke(error)
            })

        Volley.newRequestQueue(context)
            .add(request)
    }

    fun dummyFilms(): List<Film> {
        return (0..9).map { i ->
            Film(
                title = "Film $i",
                overview = "Overview $i",
                genre = "Genre $i",
                voteRating = i.toDouble(),
                release = "200$i-0$i-0$i"
            )
        }
    }

}