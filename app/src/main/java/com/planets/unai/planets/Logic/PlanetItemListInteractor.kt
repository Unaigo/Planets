package com.planets.unai.planets.Logic

import com.google.gson.GsonBuilder
import com.planets.unai.planets.Models.ServerModels.PlanetServer
import com.planets.unai.planets.ServerConnection.INetworkAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PlanetItemListInteractor {

    interface OnFinishedListener {
        fun onResultSuccessRequestPlanets(planetServer: PlanetServer)
        fun onResultFail(strError: String)
        fun onResultSuccessRequestGetMorePlanets(planetServer: PlanetServer)
        fun onResultSuccessRequestSearch(planetServer: PlanetServer)
    }


    fun requestPlanets(onFinishedListener: OnFinishedListener)
    {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://swapi.co/api/").build()

        val postsApi = retrofit.create(INetworkAPI::class.java)

        val response = postsApi.getAllPlanets()

        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            onFinishedListener.onResultSuccessRequestPlanets(it)
        }
    }

    fun requestGetMorePlanets(onFinishedListener: OnFinishedListener)
    {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://swapi.co/api/").build()

        val postsApi = retrofit.create(INetworkAPI::class.java)

        val response = postsApi.getNext(PlanetConentManager.NEXT_URL)

        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            onFinishedListener.onResultSuccessRequestGetMorePlanets(it)
        }
    }

    fun requestSearch(onFinishedListener: OnFinishedListener,searchText:String)
    {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://swapi.co/api/").build()

        val postsApi = retrofit.create(INetworkAPI::class.java)

        val response = postsApi.search(searchText)

        response.observeOn(AndroidSchedulers.mainThread()).subscribeOn(IoScheduler()).subscribe {
            onFinishedListener.onResultSuccessRequestSearch(it)
        }
    }


}