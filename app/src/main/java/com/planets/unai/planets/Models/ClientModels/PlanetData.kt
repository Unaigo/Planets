package com.planets.unai.planets.Models.ClientModels

import com.google.gson.internal.LinkedTreeMap

data class PlanetData(
    var climate: String,
    var created: String,
    var diameter: String,
    var edited: String,
    var films: List<String>,
    var gravity: String,
    var name: String,
    var orbital_period: String,
    var population: String,
    var residents: List<String>,
    var rotation_period: String,
    var surface_water: String,
    var terrain: String
){
    constructor() : this("", "",
        "", "", emptyList(),
        "", "", "",
        "", emptyList(),"",
        "",""
    )
}