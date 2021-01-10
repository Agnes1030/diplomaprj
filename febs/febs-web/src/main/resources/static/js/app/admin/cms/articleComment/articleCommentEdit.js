function updateArticleComment() {
    var selected = $("#articleCommentTable").bootstrapTable("getSelections");
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的评论！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个评论！');
        return;
    }
    var articleCommentId = selected[0].id;
    $.post(ctx + "admin/articleComment/getArticleComment", {"articleCommentId": articleCommentId}, function (r) {
        if (r.code === 0) {
            var $form = $('#articleComment-add');
            var $articleCommentTree = $('#articleCommentTree');
            $form.modal();
            var articleComment = r.msg;
            $("#articleComment-add-modal-title").html('修改评论');
            $form.find("input[name='id']").val(articleComment.id);
            $form.find("input[name='parentId']").val(articleComment.parentId);
            $form.find("input[name='seqNum']").val(articleComment.seqNum);
            $form.find("textarea[name='content']").val(articleComment.content);
            $(":radio[name='status'][value='" + articleComment.status + "']").prop("checked", "checked");
            $("#articleComment-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}