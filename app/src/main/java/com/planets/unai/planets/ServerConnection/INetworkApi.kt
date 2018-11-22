package com.planets.unai.planets.ServerConnection

import com.planets.unai.planets.Models.ServerModels.PlanetServer
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INetworkAPI {

    @GET("planets/")
    fun getAllPlanets(): Observable<PlanetServer>
    @GET("planets/")
    fun getNext(@Query("page") page : String): Observable<PlanetServer>
    @GET("planets/")
    fun search(@Query("search") search : String): Observable<PlanetServer>
}