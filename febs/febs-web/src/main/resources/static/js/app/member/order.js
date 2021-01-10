$(function () {
    var $orderTableForm = $(".order-table-form");
    var settings = {
        url: ctx + "member/getUserOrderList",
        queryParams: function (params) {
            return {
            	pageSize: params.limit,
            	pageNum: params.offset / params.limit + 1
            };
        },
        columns: [
        {
            field: 'ns',
            title: '订单号'
        }, {
            field: 'orderTitle',
            title: '订单标题'
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
            	var str ='<div class="btn-group"><a class="btn btn-xs btn-default" onClick="toView('+value+')" title="详情">详情</a>';
            	str +='</div>';
            	return str;
            }
        }

        ]
    };

    $MB.initTable('orderTable', settings);
});
function toView(orderId){
	  parent.$(parent.document).data('multitabs').create({
 	        iframe : true,
 	        title :'查看订单',
 	        url :  '/member/viewOrder?orderId='+orderId
 	    }, true);
}
$("#orderTable").delegate(".pay-btn","click",function(){
	
})
