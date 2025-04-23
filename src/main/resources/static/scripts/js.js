"use strict";

let countryResponseOk, countryResponseErr, cityResponseOk, cityResponseErr, weatherResponseOk, weatherResponseErr;

document.addEventListener("DOMContentLoaded", () => {
    countryResponseOk = document.getElementById("response-country-ok");
    countryResponseErr = document.getElementById("response-country-err");
    cityResponseOk = document.getElementById("response-city-ok");
    cityResponseErr = document.getElementById("response-city-err");
    weatherResponseOk = document.getElementById("response-weather-ok");
    weatherResponseErr = document.getElementById("response-weather-err");

    refreshCountryList();
    refreshCityList();
    refreshWeatherList();
});

const clearResponseMessages = () => {
    countryResponseOk.innerHTML = "&nbsp;";
    countryResponseErr.innerHTML = "&nbsp;";
    cityResponseOk.innerHTML = "&nbsp;";
    cityResponseErr.innerHTML = "&nbsp;";
    weatherResponseOk.innerHTML = "&nbsp;";
    weatherResponseErr.innerHTML = "&nbsp;";
};

const refreshCountryList = () => {
    htmx.trigger("#list-country", "refreshCountryEvent");
};

const refreshCityList = () => {
    htmx.trigger("#list-city", "refreshCityEvent");
};

const refreshWeatherList = () => {
    htmx.trigger("#list-weather", "refreshWeatherEvent");
};
