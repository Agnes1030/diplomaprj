$(function() {
    initTreeTable();
});

function initTreeTable() {
    var setting = {
	    idField: 'deptId',
        uniqueId: 'deptId',
        toolbar:'#toolbar',
        url: ctx + 'admin/dept/list',
        expandAll: true,
        expandColumn: "2",
        showColumns: true,
        columns: [{
                field: 'selectItem',
                checkbox: true
            },
            {
                title: '编号',
                field: 'deptId',
                width: '50px'
            },
            {
                title: '名称',
                field: 'deptName'
            },
            {
                title: '创建时间',
                field: 'createTime'
            }
        ],
        treeShowField: 'deptName',
	    parentIdField: 'parentId',
	    onResetView: function() {
	        $("#deptTable").treegrid({
	            initialState: 'collapsed', // 所有节点都折叠
                // 第二列做为树形节点展示列(columns中配置的列顺序索引)
	            treeColumn: 2,
	          //  expanderExpandedClass: 'mdi mdi-folder-open',  // 可自定义图标样式
	           // expanderCollapsedClass: 'mdi mdi-folder',
	        });
	        // 只展开树形的第一集节点
            // $("#deptTable").treegrid('getRootNodes').treegrid('expandAll');
            // 展开所有节点
	         $("#deptTable").treegrid('expandAll');
	    }
    };

    $MB.initTreeTable('deptTable', setting);
}

function search() {
     var opt={};
    $("#deptTable").bootstrapTable('refresh', opt);
}

function refresh() {
    search();
    $MB.refreshJsTree("deptTree");
}

function deleteDepts() {
    var ids = $("#deptTable").bootstrapTable("getSelections");
    var ids_arr = "";
    if (!ids.length) {
        $MB.n_warning("请勾选需要删除的部门！");
        return;
    }
    for (var i = 0; i < ids.length; i++) {
        ids_arr += ids[i].id;
        if (i !== (ids.length - 1)) ids_arr += ",";
    }
    $MB.confirm({
        text: "确定删除选中部门？",
        confirmButtonText: "确定删除"
    }, function() {
        $.post(ctx + 'admin/dept/delete', { "ids": ids_arr }, function(r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportDeptExcel(){
	$.post(ctx+"admin/dept/excel",$(".dept-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}

function exportDeptCsv(){
	$.post(ctx+"admin/dept/csv",$(".dept-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}