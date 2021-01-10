var _productCategoryId;
$(function () {
    var $productTableForm = $(".product-table-form");
    var settings = {
        url: ctx + "admin/product/list",
        pageSize: 10,
        toolbar:"#toolbar",
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                categoryId:_productCategoryId,
               // title: $productTableForm.find("input[name='title']").val().trim(),
              //  status: $productTableForm.find("select[name='status']").val()
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
            field: 'price',
            title: '价格'
        },{
            field: 'salesCount',
            title: '销量'
        },{
            field: 'seqNum',
            title: '序号'
        },{
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

    $MB.initTable('productTable', settings);
    createFilterTree();
});

function search() {
    $MB.refreshTable('productTable');
}

function refresh() {
    $MB.refreshTable('productTable');
}

function deleteProducts() {
    var selected = $("#productTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的商品！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }
    $MB.confirm({
        text: "确定删除选中商品？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/product/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}
function rIndexProducts(){
    $MB.confirm({
        text: "确定重新索引?比较耗时请在访问量较小时操作",
        confirmButtonText: "确定重新索引"
    }, function () {
    	var $btn = $("#reindexBtn").button('loading');
        $.post(ctx + 'admin/product/reIndexs', function (r) {
            if (r.code === 0) {
            	$btn.button("reset")
                $MB.n_success("索引完成");
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });	
}
function createFilterTree() {
    $.post(ctx + "admin/productCategory/tree", {}, function (r) {
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
                _productCategoryId=data.selected[0];
                if(_productCategoryId=='0'){
                	_productCategoryId=undefined;
                }
                refresh();
              });
        } else {
            $MB.n_danger(r.msg);
        }
    })
}
function toEdit(){
    var selected = $("#productTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的商品！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个商品！');
        return;
    }
    var productId = selected[0].id;
    window.location.href="/admin/product/edit?productId="+productId;
}
function exportProductExcel() {
    $.post(ctx + "admin/product/excel", $(".product-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportProductCsv() {
    $.post(ctx + "admin/product/csv", $(".product-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}