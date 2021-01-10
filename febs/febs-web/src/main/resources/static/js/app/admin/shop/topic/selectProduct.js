var _productCategoryId;
$(function () {
    var $productTableForm = $(".product-table-form");
    var settings = {
        url: ctx + "admin/product/list",
        pageSize: 10,
        toolbar:"#select_toolbar",
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                categoryId:_productCategoryId,
                title: $productTableForm.find("input[name='title']").val().trim(),
                status: $productTableForm.find("select[name='status']").val()
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

    $MB.initTable('selProductTable', settings);
    createFilterTree();
});

function searchSel() {
    $MB.refreshTable('selProductTable');
}

function addToTopic(){
    var selected = $("#selProductTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要加入的商品！');
        return;
    }
    $MB.confirm({
        text: "确定将这些商品加入专题？",
        confirmButtonText: "确定加入专题"
    }, function () {
        var ids = "";
        for (var i = 0; i < selected_length; i++) {
            ids += selected[i].id;
            if (i !== (selected_length - 1)) ids += ",";
        }
        // topicId 为全局变量，已经在父页面product.html中声明
        $.post(ctx + 'admin/topic/addToTopic', {"topicId":topicId,"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                $MB.refreshTable("productTable");
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
                $MB.refreshTable("selProductTable");
              });
        } else {
            $MB.n_danger(r.msg);
        }
    })
}
