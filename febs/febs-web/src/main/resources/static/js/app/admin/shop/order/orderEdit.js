function updateOrder() {
    var selected = $("#orderTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的订单！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个订单！');
        return;
    }
    var orderId = selected[0].id;
    $.get(ctx + "admin/order/getOrder", {"orderId": orderId}, function (r) {
        if (r.code === 0) {
            var $form = $('#order-add');
            $form.modal();
            var order = r.msg;
            $("#order-add-modal-title").html('修改订单');
            $form.find("input[name='title']").val(order.title);
            $form.find("input[name='slug']").val(order.slug);
            $form.find("input[name='id']").val(order.id);
            $form.find("input[name='oldtitle']").val(order.title);
            $form.find("input[name='template']").val(order.template);
            $form.find("input[name='orderId']").val(order.orderId);
            $form.find("input[name='keywords']").val(order.keywords);
            $form.find("input[name='seqNum']").val(order.seqNum);
            $form.find("input[name='stock']").val(order.stock);
            $form.find("input[name='price']").val(order.price);
            $form.find("input[name='originPrice']").val(order.originPrice);
            $form.find("input[name='limitedPrice']").val(order.limitedPrice);
            $form.find("input[name='limitedTime']").val(order.limitedTime);
            $form.find("input[name='thumbnail']").val(order.thumbnail);
            $form.find('.control-group').find('#target').attr('src',order.thumbnail)
            $form.find("textarea[name='description']").val(order.description);
            $form.find("textarea[name='summary']").val(order.summary);
            $form.find("textarea[name='content']").val(order.content);
            $(":radio[name='status'][value='" + order.status + "']").prop("checked", "checked");
            $(":radio[name='status'][value='" + order.commentStatus + "']").prop("checked", "checked");
            $("#order-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}