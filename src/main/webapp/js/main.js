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

            var i = 0;
            for (var result in data.results) {
                i++;
                $("resultTable").append('<tr><td>' + i + '</td><td>' + result.outcome + '</td><td>' + result.difficuly + '</td><td>' + result.primaryRoll + '</td><td>' + result.successfulHelpers + '</td><td>' + result.failedHelpers + '</td></tr>');
            }
        });
    });
});
