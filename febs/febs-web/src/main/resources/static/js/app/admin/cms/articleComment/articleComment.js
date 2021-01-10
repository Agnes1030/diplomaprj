$(function() {
	initArticleCommentTable();
});

function initArticleCommentTable() {
    var setting = {
        id: 'id',
        url: ctx + 'admin/articleComment/list',
        pageSize: 10,
        toolbar:'#toolbar',
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [{
                field: 'selectItem',
                checkbox: true
            },
            {
                title: '编号',
                field: 'id'
            },
            {
                title: '作者',
                field: 'author'
            },
            {
                title: '内容',
                field: 'content'
            }, 
            {
                title: '评论文章',
                field: 'title'
            },
            {
                title: '可用',
                field: 'status',
                formatter: function (value, row, index) {
                	
                    if (value === '1') return '<font class="text-success">是</font>';
                    if (value === '0') return '<font class="text-danger">否</font>';
                }
            },            
            {
                title: '排序',
                field: 'seqNum'
            },
            {
                title: '评论时间',
                field: 'createTime'
            }
        ]
    };

    $MB.initTable('articleCommentTable', setting);
}

function search() {
	$MB.refreshTable("articleCommentTable");
}
function deleteArticleComments() {
    var ids = $("#articleCommentTable").bootstrapTable("getSelections");
    var ids_arr = "";
    if (!ids.length) {
        $MB.n_warning("请勾选需要删除的评论！");
        return;
    }
    for (var i = 0; i < ids.length; i++) {
        ids_arr += ids[i].id;
        if (i !== (ids.length - 1)) ids_arr += ",";
    }
    $MB.confirm({
        text: "确定删除选中评论？",
        confirmButtonText: "确定删除"
    }, function() {
        $.post(ctx + 'admin/articleComment/delete', { "ids": ids_arr }, function(r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                $MB.refreshTable("articleCommentTable");
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportArticleCommentExcel(){
	$.post(ctx+"admin/articleComment/excel",$(".articleComment-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}

function exportArticleCommentCsv(){
	$.post(ctx+"admin/articleComment/csv",$(".articleComment-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}