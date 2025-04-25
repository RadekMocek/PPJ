"use strict";

let reportResponseOk, reportResponseErr;

document.addEventListener("DOMContentLoaded", () => {
    reportResponseOk = document.getElementById("response-reports-ok");
    reportResponseErr = document.getElementById("response-reports-err");

    refreshReportsList();
});

const clearResponseMessages = (source) => {
    reportResponseOk.innerHTML = "&nbsp;";
    reportResponseErr.innerHTML = "&nbsp;";
    //console.log("cleanin' from " + source);
};

const refreshReportsList = () => {
    htmx.trigger("#list-reports", "refreshReportsEvent");
};
