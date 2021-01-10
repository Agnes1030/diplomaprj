$(function () {
    var $singlePageTableForm = $(".singlePage-table-form");
    var settings = {
        url: ctx + "admin/singlePage/list",
        pageSize: 10,
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
               // title: $singlePageTableForm.find("input[name='title']").val().trim(),
              //  status: $singlePageTableForm.find("select[name='status']").val()
            };
        },
        columns: [{
            checkbox: true
        }, {
        	title: 'ID',
            field: 'id',
            visible: true
        }, {
            field: 'title',
            title: '标题'
        },{
            field: 'slug',
            title: 'slug'
        },{
            field: 'template',
            title: '模板'
        }, {
            field: 'keywords',
            title: '关键字'
        },{
            field: 'author',
            title: '作者'
        },{
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

    $MB.initTable('singlePageTable', settings);
});

function search() {
    $MB.refreshTable('singlePageTable');
}

function refresh() {
    $(".singlePage-table-form")[0].reset();
    $MB.refreshTable('singlePageTable');
}

function deleteSinglePages() {
    var selected = $("#singlePageTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的单页！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "确定删除选中单页？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/singlePage/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}
function toEdit(){
	    var selected = $("#singlePageTable").bootstrapTable('getSelections');
	    var selected_length = selected.length;
	    if (!selected_length) {
	        $MB.n_warning('请勾选需要修改的页面！');
	        return;
	    }
	    if (selected_length > 1) {
	        $MB.n_warning('一次只能修改一个页面！');
	        return;
	    }
	    var singlePageId = selected[0].id;	
 	    parent.$(parent.document).data('multitabs').create({
 	        iframe : true,
 	        title :'修改页面',
 	        url :  '/admin/singlePage/update?singlePageId='+singlePageId
 	    }, true);	
}
function exportSinglePageExcel() {
    $.post(ctx + "admin/singlePage/excel", $(".singlePage-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportSinglePageCsv() {
    $.post(ctx + "admin/singlePage/csv", $(".singlePage-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}