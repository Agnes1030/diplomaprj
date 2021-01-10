$(function() {
	initProductCommentTable();
});

function initProductCommentTable() {
    var setting = {
        id: 'id',
        url: ctx + 'admin/productComment/list',
        pageSize: 10,
        toolbar:"#toolbar",
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                content: $(".productComment-table-form").find("input[name='content']").val().trim()
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
                title: '评论商品',
                field: 'title'
            },
            {
                title: '审核状态',
                field: 'status',
                formatter: function (value, row, index) {
                	
                    if (value === '1') return '<font class="text-success">已通过</font>';
                    if (value === '0') return '<font class="text-danger">审核中</font>';
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

    $MB.initTable('productCommentTable', setting);
}

function search() {
	$MB.refreshTable("productCommentTable");
}
function deleteProductComments() {
    var ids = $("#productCommentTable").bootstrapTable("getSelections");
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
        $.post(ctx + 'admin/productComment/delete', { "ids": ids_arr }, function(r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                $MB.refreshTable("productCommentTable");
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportProductCommentExcel(){
	$.post(ctx+"admin/productComment/excel",$(".productComment-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}

function exportProductCommentCsv(){
	$.post(ctx+"admin/productComment/csv",$(".productComment-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}