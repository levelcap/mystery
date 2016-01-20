<script type="text/javascript">
;(function($) {
    $(document).ready(function() {
        var $img = $("#image").imgNotes({
            onAdd: function() {
                this.options.vAll = "bottom";
                this.options.hAll = "middle";
                var color = "#"+(0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6);
                var chr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(this.noteCount%26-1);
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
        $img.imgNotes("import", [	{x: "0.5", y:"0.5", note:"AFL Grand Final Trophy"},
            {x: "0.322", y:"0.269", note: "Brisbane Lions Flag"},
            {x: "0.824", y: "0.593", note: "Fluffy microphone"}]);
        var $toggle = $("#toggleEdit");
        if ($img.imgNotes("option","canEdit")) {
            $toggle.text("View");
        } else {
            $toggle.text("Edit");
        }
        $toggle.on("click", function() {
            var $this = $(this);
            if ($this.text()=="Edit") {
                $this.text("View");
                $img.imgNotes("option", "canEdit", true);
            } else {
                $this.text('Edit');
                $img.imgNotes('option', 'canEdit', false);
            }
        });
        var $export = $("#export");
        $export.on("click", function() {
            var $table = $("<table/>").addClass("gridtable");
            var notes = $img.imgNotes('export');
            $table.append("<th>X</th><th>Y</th><th>NOTE</th>");
            $.each(notes, function(index, item) {
                $table.append("<tr><td>" + item.x + "</td><td>" + item.y + "</td><td>" + item.note + "</td></tr>");
            });
            $('#txt').html($table);
        });
    });
    })(jQuery);
