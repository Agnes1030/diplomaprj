$(function () {
    var $orderTableForm = $(".order-table-form");
    var settings = {
        url: ctx + "admin/order/list",
        pageSize: 10,
        toolbar:"#toolbar",
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                title: $orderTableForm.find("input[name='title']").val().trim(),
                status: $orderTableForm.find("select[name='status']").val()
            };
        },
        columns: [{
            checkbox: true
        }, {
        	title: 'ID',
            field: 'id',
            visible: true
        },{
            field: 'ns',
            title: '订单号'
        }, {
            field: 'productType',
            title: '产品类型'
        },{
            field: 'orderTitle',
            title: '订单标题'
        },{
            field: 'buyerNickname',
            title: '购买人昵称'
        }, {
            field: 'orderTotalAmount',
            title: '总金额'
        },{
            field: 'orderRealAmount',
            title: '真实金额'
        },{
            field: 'couponAmount',
            title: '优惠金额'
        }, {
            field: 'payStatus',
            title: '支付状态',
            formatter: function (value, row, index) {
                if (value == '1') return '<font class="text-danger">未付款</font>';
                if (value == '2') return '<font class="text-success">支付宝</font>';
                if (value == '3') return '<font class="text-success">微信支付</font>';
                if (value == '4') return '<font class="text-success">网银支付</font>';
                if (value == '5') return '<font class="text-success">余额支付</font>';
            }
        },{
        	field:'tradeStatus',
        	title:'交易状态',
        	formatter:function(value,row,index){
                if (value === 1) return '<font class="text-success">交易中</font>';
                if (value === 2) return '<font class="text-success">交易完成</font>';
                if (value === 3) return '<font class="text-danger">已取消</font>';
                if (value === 4) return '<font class="text-success">申请退款中</font>';
                if (value === 5) return '<font class="text-success">拒绝退款</font>';
                if (value === 6) return '<font class="text-success">退款中</font>';
                if (value === 7) return '<font class="text-success">退款完成</font>';
                if (value === 9) return '<font class="text-success">交易结束</font>';
        	}
        },{
            field: 'createTime',
            title: '下单时间'
        },{
            field: 'id',
            title: '操作',
            formatter:function(value,row,index){
            	return '<div class="btn-group"><a class="btn btn-xs btn-default m-r-5 edit-btn" onClick="toViewOrder('+value+')"  title="详情">详情</a></div>';
            }
        }

        ]
    };

    $MB.initTable('orderTable', settings);
});

function search() {
    $MB.refreshTable('orderTable');
}

function refresh() {
    $(".order-table-form")[0].reset();
    $MB.refreshTable('orderTable');
}

function deleteOrders() {
    var selected = $("#orderTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的订单！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }
    $MB.confirm({
        text: "确定删除选中订单？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/order/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}
function toViewOrder(orderId){
	  parent.$(parent.document).data('multitabs').create({
 	        iframe : true,
 	        title :'订单详情',
 	        url :  '/admin/order/viewOrder?orderId='+orderId
 	    }, true);
}
function exportOrderExcel() {
    $.post(ctx + "admin/order/excel", $(".order-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportOrderCsv() {
    $.post(ctx + "admin/order/csv", $(".order-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}