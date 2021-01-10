$(function () {
    var $userTableForm = $(".user-table-form");
    var settings = {
        url: ctx + "admin/user/list",
        pageSize: 10,
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                username: $userTableForm.find("input[name='username']").val().trim()
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
            field: 'crateTime',
            title: '创建时间'
        }, {
            field: 'status',
            title: '状态',
            formatter: function (value, row, index) {
                if (value === '1') return '<font class="text-success">正常</font>';
                if (value === '0') return '<font class="text-danger">锁定</font>';
            }
        }

        ]
    };

    $MB.initTable('userTable', settings);
});


function searchSel() {
    $MB.refreshTable('userTable');
}

function sendCoupon(){
    var selected = $("#userTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要发放的的用户！');
        return;
    }
    $MB.confirm({
        text: "确定给这些用户发优惠券？",
        confirmButtonText: "确定加发放"
    }, function () {
        var ids = "";
        for (var i = 0; i < selected_length; i++) {
            ids += selected[i].userId;
            if (i !== (selected_length - 1)) ids += ",";
        }
        // couponId 为全局变量，已经在父页面coupunUser.html中声明
        $.post(ctx + 'admin/couponUser/send', {"couponId":couponId,"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                $MB.refreshTable("couponUserTable");
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}