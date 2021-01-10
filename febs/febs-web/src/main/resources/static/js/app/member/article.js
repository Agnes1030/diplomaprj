$(function () {
    var $articleTableForm = $(".article-table-form");
    var settings = {
        url: ctx + "member/getUserArticleList",
        method:'post',
        pageSize: 10,
        pageNumber:1,
        pagination:true,
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [{
            checkbox: true
        }, {
            field: 'title',
            title: '标题'
        }, {
            field: 'viewCount',
            title: '阅读数'
        }, {
            field: 'createTime',
            title: '发布时间'
        },{
            field: 'status',
            title: '状态',
            formatter: function (value, row, index) {
                if (value === '1') return '<font class="text-success">已发布</font>';
                if (value === '0') return '<font class="text-danger">审核中</font>';
            }
        },{
            field: 'id',
            title: '操作',
            formatter:function(value,row,index){
            	return '<div class="btn-group"><a class="btn btn-xs btn-default" href="/member/editArticle?articleId='+value+'" title="修改">修改</a></div>';
            }
        }

        ]
    };

    $MB.initTable('articleTable', settings);
});

function search() {
    $MB.refreshTable('articleTable');
}

function refresh() {
    $MB.refreshTable('articleTable');
}

function deleteArticles() {
    var selected = $("#articleTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的文章！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }
    $MB.confirm({
        text: "确定删除选中文章？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/article/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}