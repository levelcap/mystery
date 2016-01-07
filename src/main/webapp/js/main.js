var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

$(function () {
    var code = getUrlParameter("code");
    if (typeof code != 'undefined') {
        $.get("/api/auth?code=" + code, function (data) {
            console.log("Authorized Google API");
        });
    }

    $("#setUrl").click(function() {
        $.get("/api/setUrl?url=" + $("#basePage").val(), function (data) {
            console.log("Set url performed.");
        });
    });

    $("#setParsePage").click(function() {
        $.get("/api/setParsePage?url=" + $("#parsePage").val(), function (data) {
            console.log("Set parse page performed.");
        });
    });

    $("#setPrefix").click(function() {
        $.get("/api/setPrefix?prefix=" + $("#prefix").val(), function (data) {
            console.log("Set prefix performed.");
        });
    });

    $("#setTitleCut").click(function() {
        $.get("/api/setTitleCut?titleCut=" + $("#titleCut").val(), function (data) {
            console.log("Set title cut string performed.");
        });
    });
});
