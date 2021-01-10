var $userAddressForm = $("#userAddress-add-form");
$(function () {
    var settings = {
        url: ctx + "member/getUserAddressList",
        toolbar:"#toolbar",
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [
        {
            checkbox: true
        }, 
        {
            field: 'id',
            visible: false
        },       	
        {
            field: 'contactName',
            title: '联系人'
        }, {
            field: 'contactMobile',
            title: '联系电话'
        },{
            field: 'zipCode',
            title: '邮编'
        }, {
            field: 'province',
            title: '省'
        },{
            field: 'city',
            title: '市'
        },{
            field: 'addrDetails',
            title: '详细地址'
        }

        ]
    };

    $MB.initTable('addressTable', settings);
    // 绑定新增加保存
    $("#userAddress-add-button").click(function () {
        var form = $userAddressForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
    	if(flag){
            $.post(ctx + "member/userAddress/add", $userAddressForm.serialize(), function (r) {
                if (r.code === 0) {
                    closeModal();
                    $MB.n_success(r.msg);
                    $MB.refreshTable("addressTable");
                } else $MB.n_danger(r.msg);
            });    		
    	}
    });

    $("#userAddress-close-button").click(function () {
        closeModal();
    });
});
function refresh() {
    $MB.refreshTable('addressTable');
}
function deleteAddresss() {
    var selected = $("#addressTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    var contain = false;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的地址！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }    
    $MB.confirm({
        text: "确定删除选中地址？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'member/deleteAddress', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}
function closeModal() {
    $MB.closeAndRestModal("address-add");

}