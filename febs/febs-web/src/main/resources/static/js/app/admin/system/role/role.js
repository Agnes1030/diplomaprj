$(function () {
    var settings = {
        url: ctx + "admin/role/list",
        pageSize: 10,
        toolbar: '#toolbar', 
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [{
            checkbox: true
        }, {
            field: 'roleName',
            title: '角色'
        }, {
            field: 'remark',
            title: '描述'
        }, {
            field: 'createTime',
            title: '创建时间'
        }]
    };

    $MB.initTable('roleTable', settings);
});

function search() {
    $MB.refreshTable('roleTable');
}

function refresh() {
    $(".role-table-form")[0].reset();
    search();
}

function deleteRoles() {
    var selected = $("#roleTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的角色！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].roleId;
        if (i !== (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "删除选中角色将导致该角色对应账户失去相应的权限，确定删除？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/role/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportRoleExcel() {
    $.post(ctx + "admin/role/excel", $(".role-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportRoleCsv() {
    $.post(ctx + "admin/role/csv", $(".role-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}