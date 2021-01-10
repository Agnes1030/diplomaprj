function updateTopic() {
    var selected = $("#couponTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的优惠券类型！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个优惠券类型！');
        return;
    }
    var couponId = selected[0].id;
    $.post(ctx + "admin/coupon/getCoupon", {"couponId": couponId}, function (r) {
        if (r.code === 0) {
            var $form = $('#coupon-add');
            $form.modal();
            var coupon = r.msg;
            $("#coupon-add-modal-title").html('修改优惠券类型');
            $form.find("input[name='title']").val(coupon.title);
            $form.find("input[name='withAmount']").val(coupon.withAmount);
            $form.find("input[name='id']").val(coupon.id);
            $form.find("input[name='amount']").val(coupon.amount);
            $form.find("input[name='quota']").val(coupon.quota);
            $form.find("input[name='startTime']").val(coupon.startTime);
            $form.find("input[name='endTime']").val(coupon.endTime);            
            $form.find("input[name='validStartTime']").val(coupon.validStartTime);
            $form.find("input[name='validEndTime']").val(coupon.validEndTime);
            $form.find("input[name='code']").val(coupon.code);
            $form.find("input[name='icon']").val(coupon.icon);
            $form.find("select[name='type']").val(coupon.type);
            $form.find("select[name='productsType']").val(coupon.productsType);
            $('select').selectpicker('refresh');
            $(":radio[name='couStatus'][value='" + coupon.couStatus + "']").prop("checked", "checked");
            $("#coupon-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}