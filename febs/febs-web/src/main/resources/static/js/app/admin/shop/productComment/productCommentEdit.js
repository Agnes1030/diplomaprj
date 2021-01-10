function updateProductComment() {
    var selected = $("#productCommentTable").bootstrapTable("getSelections");
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的评论！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个评论！');
        return;
    }
    var productCommentId = selected[0].id;
    $.post(ctx + "admin/productComment/getProductComment", {"productCommentId": productCommentId}, function (r) {
        if (r.code === 0) {
            var $form = $('#productComment-add');
            var $productCommentTree = $('#productCommentTree');
            $form.modal();
            var productComment = r.msg;
            $("#productComment-add-modal-title").html('修改评论');
            $form.find("input[name='id']").val(productComment.id);
            $form.find("input[name='parentId']").val(productComment.parentId);
            $form.find("input[name='seqNum']").val(productComment.seqNum);
            $form.find("textarea[name='content']").val(productComment.content);
            $(":radio[name='status'][value='" + productComment.status + "']").prop("checked", "checked");
            $("#productComment-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}