$(function () {
    initUrlAndPermsList()
});

function initUrlAndPermsList() {
    // urls complete
    $.getJSON(ctx + "admin/menu/urlList", function (r) {
        $('#menu-url-list').autocomplete({
            hints: r,
            keyname: 'url',
            width: "100%",
            height: 38,
            valuename: 'url',
            showButton: false,
            onSubmit: function (text) {
                $('#menu-url').val(text);

            }
        });
        //    perms complete
        $('#menu-perms-list').autocomplete({
            hints: r,
            keyname: 'perms',
             width: "100%",
             height: 38,
            valuename: 'perms',
            showButton: false,
            onSubmit: function (text) {
                $('#menu-perms').val(text);
            }
        });
    });


}
