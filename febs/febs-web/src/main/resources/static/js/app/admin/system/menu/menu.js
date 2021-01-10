$(function () {
    initTreeTable();
    // 初始化图标选择器
    $picker = $('#iconSelect').fontIconPicker({
		source:    ['icon-heart', 'icon-search', 'icon-user', 'icon-tag', 'icon-help'],
		theme: 'fip-bootstrap',
		emptyIcon: false,
		hasSearch: true
	});
    $.ajax({
        url: ctx+'js/app/admin/system/menu/materialdesignicons.json',
        type: 'GET',
        dataType: 'json'
    }).done(function(response) {
 
        var fontello_json_icons = [];
 
        $.each(response.glyphs, function(i, v) {
            fontello_json_icons.push( v.css );
        });
 
        $picker.setIcons(fontello_json_icons);
    }).fail(function() {
        console.error('字体图标配置加载失败');
    });
});

function initTreeTable() {
    var setting = {
	    idField: 'menuId',
        uniqueId: 'menuId',
        toolbar:'#toolbar',
	    sidePagination: 'server',
	    showColumns: true,
        url: ctx + 'admin/menu/list',
        columns: [
            {
                field: 'selectItem',
                checkbox: true
            },
            {
                title: '编号',
                field: 'menuId',
                width: '50px'
            },
            {
                title: '名称',
                field: 'menuName'
            },

            {
                title: '图标',
                field: 'icon',
                formatter: function (value, item, index) {
                    return '<i class="zmdi ' + item.icon + '"></i>';
                }

            },
            {
                title: '类型',
                field: 'type',
                formatter: function (value, item, index) {
                    if (item.type === '0') return '<font class="text-pink">菜单</font>';
                    if (item.type === '1') return '<font class="text-info">按钮</font>';
                }

            },
            {
                title: '地址',
                field: 'url',
                formatter: function (value, item, index) {
                    return item.url === 'null' ? '' : item.url;
                }
            },
            {
                title: '权限标识',
                field: 'perms',
                formatter: function (value, item, index) {
                    return item.perms === 'null' ? '' : item.perms;
                }
            },
            {
                title: '创建时间',
                field: 'createTime'
            }
        ],
	    treeShowField: 'menuName',
	    parentIdField: 'parentId',
	    onResetView: function() {
	        $("#menuTable").treegrid({
	            initialState: 'collapsed', // 所有节点都折叠
                // 第二列做为树形节点展示列(columns中配置的列顺序索引)
	            treeColumn: 2,
	          //  expanderExpandedClass: 'mdi mdi-folder-open',  // 可自定义图标样式
	           // expanderCollapsedClass: 'mdi mdi-folder',
	        });
	        // 只展开树形的第一集节点
            // $("#menuTable").treegrid('getRootNodes').treegrid('expandAll');
            // 展开所有节点
	         $("#menuTable").treegrid('expandAll');
	    }

    };

    $MB.initTreeTable('menuTable', setting);
}

function search() {
    initTreeTable();
}

function refresh() {
    initTreeTable();
    $MB.refreshJsTree("menuTree", createMenuTree());
}

function deleteMenus() {
    var ids = $("#menuTable").bootstrapTable("getSelections");
    var ids_arr = "";
    if (!ids.length) {
        $MB.n_warning("请勾选需要删除的菜单或按钮！");
        return;
    }
    for (var i = 0; i < ids.length; i++) {
        ids_arr += ids[i].id;
        if (i !== (ids.length - 1)) ids_arr += ",";
    }
    $MB.confirm({
        text: "确定删除选中菜单或按钮？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/menu/delete', {"ids": ids_arr}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportMenuExcel() {
    $.post(ctx + "admin/menu/excel", $(".menu-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportMenuCsv() {
    $.post(ctx + "admin/menu/csv", $(".menu-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}