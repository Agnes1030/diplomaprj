$(function () {
    var $articleTagTableForm = $(".articleTag-table-form");
    var settings = {
        url: ctx + "admin/articleTag/list",
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
            field: 'tagName',
            title: 'TAG名称'
        },{
            field: 'template',
            title: '模板'
        },{
            field: 'counts',
            title: '文章数量'
        }, {
            field: 'createTime',
            title: '创建时间'
        }
        ]
    };

    $MB.initTable('articleTagTable', settings);
});

function search() {
    $MB.refreshTable('articleTagTable');
}

function refresh() {
    $MB.refreshTable('articleTagTable');
}

function deleteArticleTags() {
    var selected = $("#articleTagTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的标签！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }
    $MB.confirm({
        text: "确定删除选中标签？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/articleTag/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}
function exportArticleTagExcel() {
    $.post(ctx + "admin/articleTag/excel", $(".articleTag-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportArticleTagCsv() {
    $.post(ctx + "admin/articleTag/csv", $(".articleTag-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}