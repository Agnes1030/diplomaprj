$(function () {
    var $couponUserTableForm = $(".couponUser-table-form");
    var settings = {
        url: ctx + "member/couponUser/list",
        pageSize: 10,
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [ {
            field: 'title',
            title: '优惠券'
        },{
            field: 'orderNs',
            title: '订单号'
        },{
            field: 'userPaymentId',
            title: '支付记录ID'
        }, {
            field: 'useTime',
            title: '使用时间'
        },{
            field: 'createTime',
            title: '领取时间'
        }, {
            field: 'couStatus',
            title: '状态',
            formatter: function (value, row, index) {
                if (value === 1) return '<font class="text-success">未使用</font>';
                if (value === 2) return '<font class="text-success">已经使用</font>';
                if (value === 3) return '<font class="text-success">已过期</font>';
                if (value === 9) return '<font class="text-danger">不可用</font>';
            }
        }

        ]
    };

    $MB.initTable('couponUserTable', settings);
});

function search() {
    $MB.refreshTable('couponUserTable');
}

function refresh() {
    $(".couponUser-table-form")[0].reset();
    $MB.refreshTable('couponUserTable');
}
