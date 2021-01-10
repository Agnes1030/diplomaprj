$(function () {
    var settings = {
        url: ctx + "admin/user/list",
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
            field: 'userId',
            visible: false
        }, {
            field: 'username',
            title: '用户名'
        }, {
            field: 'theme',
            title: '主题'
        }, {
            field: 'avatar',
            title: '头像',
            formatter: function (value, row, index) {
                return '<img style="width: 2.6rem;border-radius: 50%;" src="' + ctx + 'img/avatar/' + value + '" >';
            }
        }, {
        	field:'amount',
        	title:'余额'
        },{
            field: 'deptName',
            title: '部门'
        }, {
            field: 'email',
            title: '邮箱'
        }, {
            field: 'mobile',
            title: '手机'
        }, {
            field: 'ssex',
            title: '性别',
            formatter: function (value, row, index) {
                if (value === '0') return '男';
                else if (value === '1') return '女';
                else return '保密';
            }
        }, {
            field: 'createTime',
            title: '创建时间'
        }, {
           field: 'status',
           title: '状态',
           formatter:function(value, row, index){ 
			var value="";
			if (row.status == '0') {
				value = '<span class="badge badge-danger">禁用</span>';
			} else if(row.status == '1') {
				value = '<span class="badge badge-success">正常</span>';
			}else {
				value = row.pType ;
			}
			return value;
		  }
       }
        ]
    };

    $MB.initTable('userTable', settings);
});

function search() {
    $MB.refreshTable('userTable');
}

function refresh() {
    $MB.refreshTable('userTable');
}

function deleteUsers() {
    var selected = $("#userTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的用户！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].userId;
        if (i !== (selected_length - 1)) ids += ",";
        if (userName === selected[i].username) contain = true;
    }
    if (contain) {
        $MB.n_warning('勾选用户中包含当前登录用户，无法删除！');
        return;
    }

    $MB.confirm({
        text: "确定删除选中用户？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/user/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportUserExcel() {
    $.post(ctx + "admin/user/excel", $(".user-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportUserCsv() {
    $.post(ctx + "admin/user/csv", $(".user-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}