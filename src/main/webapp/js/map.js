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
            },
            onEdit: function(ev, elem) {
                var $elem = $(elem);
                $('#NoteDialog').remove();
                return $('<div id="NoteDialog"></div>').dialog({
                    title: "Note Editor",
                    resizable: false,
                    modal: true,
                    height: "300",
                    width: "450",
                    position: { my: "left bottom", at: "right top", of: elem},
                    buttons: {
                        "Save": function() {
                            var txt = $('textarea', this).val();
//			Put the editied note back into the data area of the element
//			Very important that this step is included in custom callback implementations
                            $elem.data("note", txt);
                            $(this).dialog("close");
                        },
                        "Delete": function() {
                            console.log("Deleted!")
                            $elem.trigger("remove");
                            $(this).dialog("close");
                        },
                        Cancel: function() {
                            $(this).dialog("close");
                        }
                    },
                    open: function() {
                        $(this).css("overflow", "hidden");
                        var textarea = $('<textarea id="txt" style="height:100%; width:100%;">');
                        $(this).html(textarea);
//			Get the note text and put it into the textarea for editing
                        textarea.val($elem.data("note"));
                    }
                });
            },
        });
        $img.imgNotes("import", [
            {x: "0.5", y: "0.5", note: "The Greenwall"},
            {x: "0.322", y: "0.269", note: "Some Dwarves, I Guess"},
            {x: "0.824", y: "0.593", note: "Magical River"}
        ]);
    });
})(jQuery);