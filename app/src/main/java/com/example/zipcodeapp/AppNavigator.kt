package com.example.zipcodeapp

interface AppNavigator {
    fun navigateToCurrentForecast(zipcode:String)
    fun navigateToLocationEntry()
}