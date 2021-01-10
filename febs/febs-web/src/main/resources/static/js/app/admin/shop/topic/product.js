$(function () {
    var $productTableForm = $(".product-table-form");
    var settings = {
        url: ctx + "admin/topic/productList",
        pageSize: 10,
        toolbar:"#toolbar",
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                topicId:topicId,
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
});

function search() {
    $MB.refreshTable('productTable');
}

function refresh() {
    $(".product-table-form")[0].reset();
    $MB.refreshTable('productTable');
}

function deleteTopicProducts() {
    var selected = $("#productTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的商品关联！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }
    $MB.confirm({
        text: "确定删除选中商品关联？",
        confirmButtonText: "确定删除关联"
    }, function () {
        $.post(ctx + 'admin/topic/deleteTopicMapping', {"topicId":topicId,"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
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