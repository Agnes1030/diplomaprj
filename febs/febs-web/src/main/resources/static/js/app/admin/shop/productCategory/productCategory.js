$(function() {
    initTreeTable();
});

function initTreeTable() {
    var setting = {
        id: 'id',
        code: 'id',
        url: ctx + 'admin/productCategory/list',
        expandAll: true,
        expandColumn: "2",
        toolbar:'#toolbar',
        ajaxParams: {
           // name: $(".productCategory-table-form").find("input[name='productCategoryName']").val().trim()
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
                field: 'thumbnail',
                title: '缩略图',
                formatter:function(item,index){
                	return '<img style="width:200px;height:100px;" src='+item.thumbnail+'  onerror="this.onerror=\'\';this.src=\'/img/default.png\'"></img>';
                }
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
	        $("#productCategoryTable").treegrid({
	            initialState: 'collapsed', // 所有节点都折叠
                // 第二列做为树形节点展示列(columns中配置的列顺序索引)
	            treeColumn: 2,
	          //  expanderExpandedClass: 'mdi mdi-folder-open',  // 可自定义图标样式
	           // expanderCollapsedClass: 'mdi mdi-folder',
	        });
	        // 只展开树形的第一集节点
            // $("#deptTable").treegrid('getRootNodes').treegrid('expandAll');
            // 展开所有节点
	         $("#productCategoryTable").treegrid('expandAll');
	    }
    };

    $MB.initTreeTable('productCategoryTable', setting);
}

function search() {
    initTreeTable();
}

function refresh() {
    search();
    $MB.refreshJsTree("productCategoryTree", createProductCategoryTree());
}

function deleteCategorys() {
    var ids = $("#productCategoryTable").bootstrapTable("getSelections");
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
        $.post(ctx + 'admin/productCategory/delete', { "ids": ids_arr }, function(r) {
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
	$.post(ctx+"admin/productCategory/excel",$(".productCategory-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}

function exportCategoryCsv(){
	$.post(ctx+"admin/productCategory/csv",$(".productCategory-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}