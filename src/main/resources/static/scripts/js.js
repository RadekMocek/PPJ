"use strict";

let countryResponseOk, countryResponseErr;

document.addEventListener("DOMContentLoaded", () => {
    countryResponseOk = document.getElementById("response-country-ok");
    countryResponseErr = document.getElementById("response-country-err");

    refreshCountryList();
});

const clearResponseMessages = () => {
    countryResponseOk.innerHTML = "&nbsp;";
    countryResponseErr.innerHTML = "&nbsp;";
};

const refreshCountryList = () => {
    htmx.trigger("#list-country", "refreshCountryEvent");
};
