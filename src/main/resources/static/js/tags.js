let gtag = [];
let tag = [];
$('.tagInput').on("keyup", function() {
    if (($(this).val().match(/[^a-zа-яA-ZА-Я0-9,; ]/g)||[]).length === 1) {
        document.getElementById("errorCode").hidden = false;
    }
    if(($(this).val().match(/[^a-zа-яA-ZА-Я0-9,; ]/g)||[]).length > 1){
        document.getElementById("errorCode").hidden = false;
        $(this).val($(this).val().slice(0, -1));
    }
    if(($(this).val().match(/[^a-zа-яA-ZА-Я0-9,; ]/g)||[]).length === 0) {
        document.getElementById("errorCode").hidden = true;
    }
});

$(function() {
    function log( message ) {
        $( "<div/>" ).text( message ).prependTo( "#log" );
    }

    function split(val) {
        return val.split( /(,\s*)|(;\s*)|( \s*)/ );
    }

    function extractLast(term) {
        return split(term).pop();
    }
    $(this).prev().val('');
    $(this).val('');

    $("#tagInput")
        .on("keydown", function (event) {
            if (event.keyCode === $.ui.keyCode.TAB &&
                $(this).autocomplete("instance").menu.active) {
                event.preventDefault();
            }
        })
        .autocomplete({
            mathCase: true,
            source: function (request, response) {
                $.ajax({
                    url: "/api/user/tag/latter",
                    headers: {
                        'Authorization': 'Bearer ' + token
                    },
                    dataType: "json",
                    data: "searchString=" + extractLast(request.term),
                    success: function (data) {

                        response(
                            $.map(data, function (item) {
                                return {
                                    value: item.name,
                                    description: item.description,
                                    id: item.id
                                }
                            }))
                    }
                });
            },
            search: function () {
                let term = extractLast(this.value);
                if (term.length < 1) {
                    return false;
                }
            },
            select:
                function (event, ui) {
                    let terms = split(this.value);
                    terms.pop();
                    terms.push(ui.item.value);
                    terms.push("");
                    this.value = terms.join("");
                    log(`{"name":"${ui.item.value}", "id": ${ui.item.id}, "description": "${ui.item.description}"}, ` );
                    gtag = document.getElementById("log").textContent;
                    tag = JSON.stringify(JSON.parse("[" + gtag.slice(0, -2) + "]"));
                    return false;
                }
        })
});
