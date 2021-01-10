$(function() {
    initTreeTable();
});

function initTreeTable() {
    var setting = {
        id: 'id',
        code: 'id',
        url: ctx + 'admin/navMenu/list',
        toolbar:"#toolbar",
        expandAll: true,
        expandColumn: "2",
        ajaxParams: {
            name: $(".navMenu-table-form").find("input[name='navMenuName']").val().trim()
        },
        columns: [{
                field: 'selectItem',
                checkbox: true
            },
            {
                title: '编号',
                field: 'id',
                width: '50px'
            },
            {
                title: '名称',
                field: 'name'
            },
            {
                title: '访问地址',
                field: 'navUrl'
            },
            {
                title: '类型',
                field: 'navType',
                formatter: function (item, index) {
                    if (item.navType === 'page') return '<font class="text-success">单页</font>';
                    else if (item.navType === 'category') return '<font class="text-success">文章</font>';
                    else if (item.navType === 'productCategory') return '<font class="text-success">商品</font>';
                    else if (item.navType === 'productTopic') return '<font class="text-success">专题</font>';
                    
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
                title: '目标',
                field: 'target',
                formatter: function (item, index) {
                    if (item.target === '_self') return '<font class="text-success">当前窗口</font>';
                    if (item.status === '_blank') return '<font class="text-success">新打开</font>';
                }
            },
            {
                title: '创建时间',
                field: 'createTime'
            }
        ],
        treeShowField: 'name',
	    parentIdField: 'parentId',
	    onResetView: function() {
	        $("#navMenuTable").treegrid({
	            initialState: 'collapsed', // 所有节点都折叠
                // 第二列做为树形节点展示列(columns中配置的列顺序索引)
	            treeColumn: 2,
	          //  expanderExpandedClass: 'mdi mdi-folder-open',  // 可自定义图标样式
	           // expanderCollapsedClass: 'mdi mdi-folder',
	        });
	        // 只展开树形的第一集节点
            // $("#deptTable").treegrid('getRootNodes').treegrid('expandAll');
            // 展开所有节点
	         $("#navMenuTable").treegrid('expandAll');
	    }
    };

    $MB.initTreeTable('navMenuTable', setting);
}

function search() {
    initTreeTable();
}

function refresh() {
    $(".navMenu-table-form")[0].reset();
    search();
    $MB.refreshJsTree("navMenuTree", createNavMenuTree());
}

function deleteNavMenus() {
    var ids = $("#navMenuTable").bootstrapTable("getSelections");
    var ids_arr = "";
    if (!ids.length) {
        $MB.n_warning("请勾选需要删除的导航！");
        return;
    }
    for (var i = 0; i < ids.length; i++) {
        ids_arr += ids[i].id;
        if (i !== (ids.length - 1)) ids_arr += ",";
    }
    $MB.confirm({
        text: "确定删除选中 导航？",
        confirmButtonText: "确定删除"
    }, function() {
        $.post(ctx + 'admin/navMenu/delete', { "ids": ids_arr }, function(r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportNavMenuExcel(){
	$.post(ctx+"admin/navMenu/excel",$(".navMenu-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}

function exportNavMenuCsv(){
	$.post(ctx+"admin/navMenu/csv",$(".navMenu-table-form").serialize(),function(r){
		if (r.code === 0) {
			window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}