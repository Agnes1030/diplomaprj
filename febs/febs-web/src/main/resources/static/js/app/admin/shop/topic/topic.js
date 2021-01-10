$(function () {
    var $topicTableForm = $(".topic-table-form");
    var settings = {
        url: ctx + "admin/topic/list",
        pageSize: 10,
        toolbar:"#toolbar",
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
            title: '专题名称'
        }, {
            field: 'thumbnail',
            title: '缩略图',
            formatter:function(item,index){
            	return '<img style="width:200px;height:100px;" src='+item+'  onerror="this.onerror=\'\';this.src=\'/img/default.png\'"></img>';
            }
        }, {
            field: 'descriptions',
            title: '描述'
        }, {
            field: 'createTime',
            title: '创建时间'
        }, 
        {
            title: '导航显示',
            field: 'navShow',
            formatter: function (value, row, index) {
                if (value === '1') return '<font class="text-success">是</font>';
                if (value === '0') return '<font class="text-danger">否</font>';
            }
        },
        {
            field: 'recommend',
            title: '推荐',
            formatter: function (value, row, index) {
                if (value === '1') return '<font class="text-success">是</font>';
                if (value === '0') return '<font class="text-danger">否</font>';
            }
        }, {
            field: 'id',
            title: '操作',
            formatter: function (value, row, index) {
            	return '<div class="btn-group"><a class="btn btn-xs btn-default" href="/admin/topic/viewTopic?topicId='+value+'" title="商品">商品</a></div>';
            }
        }

        ]
    };

    $MB.initTable('topicTable', settings);
});

function search() {
    $MB.refreshTable('topicTable');
}

function refresh() {
    $(".topic-table-form")[0].reset();
    $MB.refreshTable('topicTable');
}

function deleteTopics() {
    var selected = $("#topicTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的专题！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "确定删除选中专题？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/topic/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}
