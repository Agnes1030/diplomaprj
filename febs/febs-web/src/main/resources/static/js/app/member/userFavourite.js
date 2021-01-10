$(function () {
    var $orderTableForm = $(".order-table-form");
    var settings = {
        url: ctx + "member/favourites/list",
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
            visible: false
        },
        {
            field: 'title',
            title: '标题'
        }, {
            field: 'type',
            title: '收藏类型',
            formatter:function(value,row,index){
            	if(value=="product") return '<font class="text-success">商品</font>';
            	if(value=="article") return '<font class="text-success">文章</font>';
            }
        },{
            field: 'createTime',
            title: '收藏时间'
        },{
            field: 'favouriteId',
            title: '查看',
            formatter:function(value,row,index){
            	if(row.type=="product") return '<div class="btn-group"><a class="btn btn-xs btn-default" href="/product/view/'+value+'" title="查看" target="_blank">查看</a></div>'
            	if(row.type=="article") return '<div class="btn-group"><a class="btn btn-xs btn-default" href="/article/view/'+value+'" title="查看" target="_blank">查看</a></div>'
            }
        }

        ]
    };

    $MB.initTable('favouritesTable', settings);
});
function refresh() {
    $MB.refreshTable('favouritesTable');
}
function deleteFavourites() {
    var selected = $("#favouritesTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的收藏！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }
    $MB.confirm({
        text: "确定删除选中收藏？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'member/favourites/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}