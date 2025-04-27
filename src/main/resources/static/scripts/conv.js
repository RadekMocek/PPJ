"use strict";

const conv = () => {
    [...document.getElementsByClassName("conv-temperature")].forEach(el => {
        el.textContent = twoDecimalsAndCelsius(el.textContent);
    });
    [...document.getElementsByClassName("conv-datestamp")].forEach(el => {
        el.textContent = unixToHuman(el.textContent);
    });
};

const twoDecimalsAndCelsius = (number) => parseFloat(number).toFixed(2) + " °C";

const unixToHuman = (timestamp) => {
    const date = new Date(timestamp * 1000);

    return `${date.getFullYear()}-${pad2z(date.getMonth() + 1)}-${pad2z(date.getDate())}`;
};

const pad2z = (n) => (`${new Array(2).fill(0)}${n}`).slice(-2);
