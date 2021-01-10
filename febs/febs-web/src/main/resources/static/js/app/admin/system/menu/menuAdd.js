var $menuAddForm = $("#menu-add-form");
var $menuName = $menuAddForm.find("input[name='menuName']");
var $url = $menuAddForm.find("input[name='url']");
var $icon = $menuAddForm.find("input[name='icon']");
var $menuUrlListRow = $menuAddForm.find(".menu-url-list-row");
var $menuIconListRow = $menuAddForm.find(".menu-icon-list-row");
var $menuPermsListRow = $menuAddForm.find(".menu-perms-list-row");
var $icon_drop = $menuAddForm.find(".icon-drop");

$(function () {
    createMenuTree();
    $menuAddForm.find("input[name='type']").change(function () {
        var $value = $menuAddForm.find("input[name='type']:checked").val();
        if ($value === "0") {
            $menuName.parent().prev().text("菜单名称：");
            $menuUrlListRow.show();
            $menuIconListRow.show();
        } else {
            $menuName.parent().prev().text("按钮名称：");
            $menuUrlListRow.hide();
            $menuIconListRow.hide();
        }
    });
    $("#menu-add").click(function (event) {
        var obj = event.srcElement || event.target;
        if (!$(obj).is("input[name='icon']")) {
            $icon_drop.hide();
        }
    });

    $icon_drop.find(".menu-icon .col-sm-6").on("click", function () {
        var icon = "zmdi " + $(this).find("i").attr("class").split(" ")[1];
        $icon.val(icon);
    });
    $("#menu-add .btn-save").click(function () {
        $menuPermsListRow.find("input[name='perms']").val(
            $menuPermsListRow.find(".autocomplete-input").val()
        );
        $menuUrlListRow.find("input[name='url']").val(
            $menuUrlListRow.find(".autocomplete-input").val()
        );
        var name = $(this).attr("name");
        getMenu();
        var form = $menuAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/menu/add", $menuAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        refresh();
                        closeModal();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/menu/update", $menuAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        refresh();
                        closeModal();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#menu-add .btn-close").click(function () {
        $("input:radio[value='0']").trigger("click");
        closeModal();
    });

});

function closeModal() {
    $menuName.parent().prev().text("菜单名称：");
    $("#menu-add-modal-title").html('新增菜单/按钮');
    $("#menu-add-button").attr("name", "save");
    $menuUrlListRow.show();
    $menuIconListRow.show();
    $("#menuIcon .selected-icon>.mdi-access-point").attr("class","mdi mdi-access-point");
    $MB.closeAndRestModal("menu-add");
    $MB.refreshJsTree("menuTree", createMenuTree());

}

function createMenuTree() {
    $.post(ctx + "admin/menu/tree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#menuTree').jstree({
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

function getMenu() {
    var ref = $('#menuTree').jstree(true);
    $("[name='parentId']").val(ref.get_checked()[0]);
}