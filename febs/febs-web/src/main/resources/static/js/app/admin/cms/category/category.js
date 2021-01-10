$(function() {
    initTreeTable();
});

function initTreeTable() {
    var setting = {
	    idField: 'id',
        uniqueId: 'id',
        code: 'id',
        url: ctx + 'admin/category/list',
        expandAll: true,
        expandColumn: "2",
        showColumns: true,
        ajaxParams: {
           // name: $(".category-table-form").find("input[name='categoryName']").val().trim()
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
                title: '名称',
                field: 'name'
            },
            {
                title: 'slug',
                field: 'slug'
            },
            {
                title: '模板',
                field: 'template'
            },
            {
                title: '导航显示',
                field: 'navShow',
                formatter: function (item, index) {
                    if (item.navShow === '1') return '<font class="text-success">是</font>';
                    if (item.navShow === '0') return '<font class="text-danger">否</font>';
                }
            },           
            {
                title: '可用',
                field: 'status',
                formatter: function (item, index) {
                    if (item.status === '1') return '<font class="text-success">是</font>';
                    if (item.status === '0') return '<font class="text-danger">否</font>';
                }
            },            
            {
                title: '排序',
                field: 'seqNum'
            },
            {
                title: '创建时间',
                field: 'createTime'
            }
        ],
        treeShowField: 'name',
	    parentIdField: 'parentId',
	    onResetView: function() {
	        $("#categoryTable").treegrid({
	            initialState: 'collapsed', // 所有节点都折叠
                // 第二列做为树形节点展示列(columns中配置的列顺序索引)
	            treeColumn: 2,
	          //  expanderExpandedClass: 'mdi mdi-folder-open',  // 可自定义图标样式
	           // expanderCollapsedClass: 'mdi mdi-folder',
	        });
	        // 只展开树形的第一集节点
            // $("#deptTable").treegrid('getRootNodes').treegrid('expandAll');
            // 展开所有节点
	         $("#categoryTable").treegrid('expandAll');
	    }
    };

    $MB.initTreeTable('categoryTable', setting);
}

function search() {
   // 带参数，刷新（加载新请求数据）
    var opt={};
    $("#categoryTable").bootstrapTable('refresh', opt);
}

function refresh() {
    search();
    $MB.refreshJsTree("categoryTree",createCategoryTree());
}

function deleteCategorys() {
    var ids = $("#categoryTable").bootstrapTable("getSelections");
    var ids_arr = "";
    if (!ids.length) {
        $MB.n_warning("请勾选需要删除的分类！");
        return;
    }
    for (var i = 0; i < ids.length; i++) {
        ids_arr += ids[i].id;
        if (i !== (ids.length - 1)) ids_arr += ",";
    }
    $MB.confirm({
        text: "确定删除选中分类？",
        confirmButtonText: "确定删除"
    }, function() {
        $.post(ctx + 'admin/category/delete', { "ids": ids_arr }, function(r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportCategoryExcel(){
	$.post(ctx+"admin/category/excel",$(".category-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}

function exportCategoryCsv(){
	$.post(ctx+"admin/category/csv",$(".category-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}