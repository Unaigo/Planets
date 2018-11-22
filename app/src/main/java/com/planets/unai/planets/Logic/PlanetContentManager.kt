package com.planets.unai.planets.Logic

import com.planets.unai.planets.Models.ClientModels.PlanetData
import com.planets.unai.planets.Models.ServerModels.PlanetServer

object PlanetConentManager
{
    val ITEM_MAP: MutableMap<String, PlanetData> = HashMap()

    val ITEMS: MutableList<PlanetData> = ArrayList()

    var NEXT_URL: String = "https://swapi.co/api/";

    var ITEMS_SEARCH : MutableList<PlanetData> = ArrayList()

    fun setPlanetsServerAndFactory(planetServer: PlanetServer)
    {
        //Factory from server model to ClientModel and setting to vals
        planetServer.results.forEach {

            val planetsData: PlanetData = PlanetData()
            planetsData.name = it.name
            planetsData.climate =it.climate
            planetsData.created = it.created
            planetsData.diameter =it.diameter
            planetsData.edited = it.edited
            planetsData.films = it.films
            planetsData.gravity = it.gravity
            planetsData.orbital_period = it.orbital_period
            planetsData.population = it.population
            planetsData.residents = it.residents
            planetsData.rotation_period = it.rotation_period
            planetsData.surface_water = it.surface_water
            planetsData.terrain = it.terrain
            ITEMS.add(planetsData)
            ITEM_MAP.put(planetsData.name, planetsData)
        }
        //COPY URL TO LOAD MORE
        if(planetServer.next==null || NEXT_URL == planetServer.next.takeLast(1))
        {
            NEXT_URL = "";
        }
        else
        {
            NEXT_URL = planetServer.next.takeLast(1);
        }


    }

    fun setPlanetsServerAndFactoryFromSearch(planetServer: PlanetServer)
    {
        //Factory from server model to ClientModel and setting to vals
        planetServer.results.forEach {

            val planetsData: PlanetData = PlanetData()
            planetsData.name = it.name
            planetsData.climate =it.climate
            planetsData.created = it.created
            planetsData.diameter =it.diameter
            planetsData.edited = it.edited
            planetsData.films = it.films
            planetsData.gravity = it.gravity
            planetsData.orbital_period = it.orbital_period
            planetsData.population = it.population
            planetsData.residents = it.residents
            planetsData.rotation_period = it.rotation_period
            planetsData.surface_water = it.surface_water
            planetsData.terrain = it.terrain
            ITEMS_SEARCH.add(planetsData)
            ITEM_MAP.put(planetsData.name, planetsData)
        }


    }

}