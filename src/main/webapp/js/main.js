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
    $("#fateIt").click(function() {
        $.get("/api/fate?difficulty=" + $("#difficulty").val() + "&bonus=" + $("#bonus").val() + "&advantageDifficulty=" + $("#advantageDifficulty").val() + "&helpers=" + $("#helpers").val() + "&run=" + $("#run").val(), function (data) {
            console.log(data);
            $("#failSpan").text(data.fails);
            $("#tieSpan").text(data.ties);
            $("#successSpan").text(data.successes);
            $("#withstyleSpan").text(data.withStyles);
            $("#average").text(data.avgAdvantage);

            var i = 0;;
            $("#resultTableBody").html("");
            for (i = 0; i < data.results.length; i++) {
                var result = data.results[i];
                var runNum = i + 1;
                $("#resultTableBody").append('<tr><td>' + runNum + '</td><td>' + result.outcome + '</td><td>' + result.difficulty  + '</td><td>' + result.primaryRoll + '</td><td>' + result.successfulHelpers + '</td><td>' + result.failedHelpers + '</td><td>' + result.booster + '</td></tr>');
            }
        });
    });
});
