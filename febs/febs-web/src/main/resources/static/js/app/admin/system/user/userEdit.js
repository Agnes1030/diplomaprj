function updateUser() {
    var selected = $("#userTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的用户！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个用户！');
        return;
    }
    var userId = selected[0].userId;
    $.post(ctx + "admin/user/getUser", {"userId": userId}, function (r) {
        if (r.code === 0) {
            var $form = $('#user-add');
            var $deptTree = $('#deptTree');
            $form.modal();
            var user = r.msg;
            $form.find(".user_password").hide();
            $("#user-add-modal-title").html('修改用户');
            $form.find("input[name='username']").val(user.username).attr("readonly", true);
            $form.find("input[name='oldusername']").val(user.username);
            $form.find("input[name='userId']").val(user.userId);
            $form.find("input[name='email']").val(user.email);
            var roleArr = [];
            for (var i = 0; i < user.roleIds.length; i++) {
                roleArr.push(user.roleIds[i]);
            }
            $('#rolesSelect').selectpicker('val',roleArr);
            $form.find("input[name='roles']").val(user.roleIds);
            $(":radio[name='status'][value='" + user.status + "']").prop("checked", "checked");
            $(":radio[name='ssex'][value='" + user.ssex + "']").prop("checked", "checked");
            $deptTree.jstree().open_all();
            $deptTree.jstree('select_node', user.deptId, true);
            $("#user-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}