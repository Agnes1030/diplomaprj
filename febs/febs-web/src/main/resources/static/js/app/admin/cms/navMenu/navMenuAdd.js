var $navMenuAddForm = $("#navMenu-add-form");
var $navMenuSelect = $navMenuAddForm.find("select[name='target']");
var $navTypeSelect = $navMenuAddForm.find("select[name='navType']");
$(function () {
    createNavMenuTree();
    $("#navMenu-add .btn-save").click(function () {
        var name = $(this).attr("name");
        getNavMenu();
        var form = $navMenuAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/navMenu/add", $navMenuAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        refresh();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/navMenu/update", $navMenuAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        refresh();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#navMenu-add .btn-close").click(function () {
        closeModal();
    });

});
function closeModal() {
    $("#navMenu-add-button").attr("name", "save");
    $("#navMenu-add-modal-title").html('新增导航');
    $MB.closeAndRestModal("navMenu-add");
    $MB.refreshJsTree("navMenuTree", createNavMenuTree());
}
function createNavMenuTree() {
    $.post(ctx + "admin/navMenu/tree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#navMenuTree').jstree({
                "core": {
                    'data': data.children,
                    'multiple': false
                },
                "state": {
                    "disabled": true
                },
                "checkbox": {
                    "three_state": false
                },
                "plugins": ["wholerow", "checkbox"]
            });
        } else {
            $MB.n_danger(r.msg);
        }
    })

}

function getNavMenu() {
    var ref = $('#navMenuTree').jstree(true);
    var navMenuIds = ref.get_checked();
    $("[name='parentId']").val(navMenuIds[0]);
}