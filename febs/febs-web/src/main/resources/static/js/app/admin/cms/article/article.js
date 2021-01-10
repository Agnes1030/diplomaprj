var _categoryId;
$(function () {
    var $articleTableForm = $(".article-table-form");
    var settings = {
        url: ctx + "admin/article/list",
        pageSize: 10,
        toolbar:'#toolbar',
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                categoryId:_categoryId
            };
        },
        columns: [{
            checkbox: true
        }, {
        	title: 'ID',
            field: 'id',
            visible: true
        },{
            field: 'title',
            title: '标题'
        }, {
            field: 'slug',
            title: 'slug'
        },{
            field: 'template',
            title: '模板'
        },{
            field: 'author',
            title: '作者'
        }, {
            field: 'seqNum',
            title: '序号'
        }, {
            field: 'viewCount',
            title: '阅读数'
        }, {
            field: 'createTime',
            title: '发布时间'
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

    $MB.initTable('articleTable', settings);
    createFilterTree();
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
function createFilterTree() {
    $.post(ctx + "admin/category/tree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#filterTree').jstree({
                "core": {
                    'data': data,
                    'multiple': true // 取消多选
                },
                "plugins": ["wholerow"]
            });
            $('#filterTree').on("changed.jstree", function (e, data) {
                _categoryId=data.selected[0];
                if(_categoryId=='0'){
                	_categoryId=undefined;
                }
                refresh();
              });
        } else {
            $MB.n_danger(r.msg);
        }
    })
}
function toEdit(){
	    var selected = $("#articleTable").bootstrapTable('getSelections');
	    var selected_length = selected.length;
	    if (!selected_length) {
	        $MB.n_warning('请勾选需要修改的文章！');
	        return;
	    }
	    if (selected_length > 1) {
	        $MB.n_warning('一次只能修改一个文章！');
	        return;
	    }
	    var articleId = selected[0].id;	
 	    parent.$(parent.document).data('multitabs').create({
 	        iframe : true,
 	        title :'修改文章',
 	        url :  '/admin/article/update?articleId='+articleId
 	    }, true);	
}
function exportArticleExcel() {
    $.post(ctx + "admin/article/excel", $(".article-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportArticleCsv() {
    $.post(ctx + "admin/article/csv", $(".article-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}