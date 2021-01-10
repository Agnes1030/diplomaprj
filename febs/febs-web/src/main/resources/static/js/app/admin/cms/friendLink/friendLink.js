$(function () {
    var $friendLinkTableForm = $(".friendLink-table-form");
    var settings = {
        url: ctx + "admin/friendLink/list",
        pageSize: 10,
        toolbar:'#toolbar',
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [{
            checkbox: true
        }, {
        	title: 'ID',
            field: 'id',
            visible: true
        },{
            field: 'name',
            title: '名称'
        }, {
            field: 'target',
            title: '打开方式'
        },{
            field: 'urlAddress',
            title: '链接'
        }, {
            field: 'seqNum',
            title: '序号'
        }, {
            field: 'thumbnail',
            title: '图片',
            formatter:function(value,item,index){
            	return '<img style="width:200px;height:100px;" src=\''+value+'\'  onerror="this.onerror=\'\';this.src=\'/img/default.png\'" ></img>';
            }
        }, {
            field: 'createTime',
            title: '创建时间'
        }, {
            field: 'status',
            title: '状态',
            formatter: function (value, row, index) {
                if (value === '1') return '<font class="text-success">有效</font>';
                if (value === '0') return '<font class="text-danger">锁定</font>';
            }
        }

        ]
    };

    $MB.initTable('friendLinkTable', settings);
});

function search() {
    $MB.refreshTable('friendLinkTable');
}

function refresh() {
    $MB.refreshTable('friendLinkTable');
}

function deleteFriendLinks() {
    var selected = $("#friendLinkTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的友情链接！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "确定删除选中友情链接？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/friendLink/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportFriendLinkExcel() {
    $.post(ctx + "admin/friendLink/excel", $(".friendLink-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportFriendLinkCsv() {
    $.post(ctx + "admin/friendLink/csv", $(".friendLink-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}