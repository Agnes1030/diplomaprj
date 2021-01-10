function updateArticleTag() {
    var selected = $("#articleTagTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的标签！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个标签！');
        return;
    }
    var articleTagId = selected[0].id;
    $.post(ctx + "admin/articleTag/getArticleTag", {"articleTagId": articleTagId}, function (r) {
        if (r.code === 0) {
            var $form = $('#articleTag-add');
            $form.modal();
            var articleTag = r.msg;
            $("#articleTag-add-modal-title").html('修改标签');
            $form.find("input[name='tagName']").val(articleTag.tagName);
            $form.find("input[name='template']").val(articleTag.template);
            $form.find("input[name='id']").val(articleTag.id);
            $("#articleTag-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}