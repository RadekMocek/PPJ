"use strict";

let modalCountry, modalCountryClose, editCountryCode, editCountryName;

document.addEventListener("DOMContentLoaded", () => {
    modalCountry = document.getElementById("modal-country");
    modalCountryClose = document.getElementById("modal-country-close");
    modalCountryClose.onclick = () => {
        modalCountry.style.display = "none";
    };
    editCountryCode = document.getElementById("country-edit-code");
    editCountryName = document.getElementById("country-edit-name");
});

const showModal = (which, what, how) => {
    if (which === "country") {
        modalCountry.style.display = "block";
        editCountryCode.value = what;
        editCountryName.value = how;
    }
};

const closeModals = () => {
    modalCountry.style.display = "none";
};

window.onclick = (event) => {
    if (event.target === modalCountry) {
        modalCountry.style.display = "none";
    }
};
