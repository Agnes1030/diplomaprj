var $roleAddForm = $("#role-add-form");
$(function () {
    createMenuTree();
    $("#role-add .btn-save").click(function () {
	    getMenu();
        var name = $(this).attr("name");
        var form = $roleAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        $("#roles").val($('#rolesSelect').val());
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/role/add", $roleAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("roleTable");
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/role/update", $roleAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("roleTable");
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#role-add .btn-close").click(function () {
        closeModal();
    });

});

function closeModal() {
    $("#role-add-button").attr("name", "save");
    $("#role-add-modal-title").html('新增角色');
    $MB.resetJsTree("menuTree");
    $MB.closeAndRestModal("role-add");
}
function createMenuTree() {
    $.post(ctx + "admin/menu/menuButtonTree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#menuTree').jstree({
                "core": {
                    'data': data.children
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
    var $menuTree = $('#menuTree');
    var ref = $menuTree.jstree(true);
    var menuIds = ref.get_checked();
    $menuTree.find(".jstree-undetermined").each(function (i, element) {
        menuIds.push($(element).closest('.jstree-node').attr("id"));
    });
    $("[name='menuId']").val(menuIds);
}