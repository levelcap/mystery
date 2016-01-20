;
(function ($) {
    $(document).ready(function () {
        var $img = $("#image").imgNotes({
            canEdit: true,
            onAdd: function () {
                this.options.vAll = "bottom";
                this.options.hAll = "middle";
                var color = "#" + (0x1000000 + (Math.random()) * 0xffffff).toString(16).substr(1, 6);
                var chr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(this.noteCount % 26 - 1);
                var elem = $(document.createElement('div')).css({
                    "background-color": color,
                    height: "40px",
                    width: "15px",
                    "border-radius": "60px",
                    "text-align": "center",
                    color: "#fff"
                }).html(chr);
                return elem;
            }
        });
        $img.imgNotes("import", [
            {x: "0.5", y: "0.5", note: "AFL Grand Final Trophy"},
            {x: "0.322", y: "0.269", note: "Brisbane Lions Flag"},
            {x: "0.824", y: "0.593", note: "Fluffy microphone"}
        ]);
    });
})(jQuery);