var $userAddForm = $("#user-add-form");
$(function () {
    initRole();
    createDeptTree();
    $("#user-add-button").click(function () {
        var name = $(this).attr("name");
        getDept();
        var form = $userAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        $("#roles").val($('#rolesSelect').val());
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/user/add", $userAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("userTable");
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/user/update", $userAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("userTable");
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#user-close-button").click(function () {
        closeModal();
    });

});

function closeModal() {
    $("#user-add-button").attr("name", "save");
    $userAddForm.find("input[name='username']").removeAttr("readonly");
    $userAddForm.find(".user_password").show();
    $("#user-add-modal-title").html('新增用户');
    $MB.resetJsTree("deptTree");
    $MB.closeAndRestModal("user-add");
}
function initRole() {
	var $rolesSelect = $userAddForm.find("select[name='rolesSelect']");
    var $roles = $userAddForm.find("input[name='roles']");
    $.get(ctx + "admin/role/list", {pageSize:10,pageNumber:1}, function (r) {
        var data = r.rows;
        var option = "";
        for (var i = 0; i < data.length; i++) {
            option += "<option value='"+data[i].roleId+"'>" + data[i].remark + "</option>"
        }
        $rolesSelect.html(option);
        $('#rolesSelect').selectpicker();
    });
}

function createDeptTree() {
    $.post(ctx + "admin/dept/tree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#deptTree').jstree({
                "core": {
                    'data': data.children,
                    'multiple': false // 取消多选
                },
                "state": {
                    "disabled": true
                },
                "checkbox": {
                    "three_state": false // 取消选择父节点后选中所有子节点
                },
                "plugins": ["wholerow", "checkbox"]
            });
        } else {
            $MB.n_danger(r.msg);
        }
    })
}

function getDept() {
    var ref = $('#deptTree').jstree(true);
    $("[name='deptId']").val(ref.get_selected()[0]);
}