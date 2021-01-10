function updateTopic() {
    var selected = $("#topicTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的专题！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个专题！');
        return;
    }
    var topicId = selected[0].id;
    $.post(ctx + "admin/topic/getProductTopic", {"topicId": topicId}, function (r) {
        if (r.code === 0) {
            var $form = $('#topic-add');
            $form.modal();
            var topic = r.msg;
            $("#topic-add-modal-title").html('修改专题');
            $form.find("input[name='name']").val(topic.name);
            $form.find("input[name='slug']").val(topic.slug);
            $form.find("input[name='id']").val(topic.id);
            $form.find("input[name='seoKeywords']").val(topic.seoKeywords);
            $form.find("input[name='template']").val(topic.template);
            $form.find("input[name='detailTemplate']").val(topic.detailTemplate);
            $form.find("input[name='thumbnail']").val(topic.thumbnail);
            $form.find("textarea[name='seoDescriptions']").val(topic.seoDescriptions);
            $form.find("textarea[name='descriptions']").val(topic.descriptions);
            $(":radio[name='recommend'][value='" + topic.recommend + "']").prop("checked", "checked");
            $(":radio[name='navShow'][value='" + topic.navShow + "']").prop("checked", "checked");
            $("#topic-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}