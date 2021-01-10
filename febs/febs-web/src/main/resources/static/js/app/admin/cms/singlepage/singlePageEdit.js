function updateSinglePage(singlePageId) {
    $.get(ctx + "admin/singlePage/getSinglePage", {"singlePageId": singlePageId}, function (r) {
        if (r.code === 0) {
            var $form = $('#singlePage-add');
            var singlePage = r.msg;
            $("#singlePage-add-modal-title").html('修改单页');
            $form.find("input[name='title']").val(singlePage.title);
            $form.find("input[name='slug']").val(singlePage.slug);
            $form.find("input[name='id']").val(singlePage.id);
            $form.find("input[name='keywords']").val(singlePage.keywords);
            $form.find("textarea[name='description']").val(singlePage.description);
            $form.find("textarea[name='content']").val(singlePage.content);
            $form.find("input[name='template']").val(singlePage.template);
            $form.find("input[name='seqNum']").val(singlePage.seqNum);
            $(":radio[name='status'][value='" + singlePage.status + "']").prop("checked", "checked");
            $(":radio[name='navShow'][value='" + singlePage.navShow + "']").prop("checked", "checked");
            CKEDITOR.instances.singlePageEditor.setData(singlePage.content);
            $("#singlePage-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}
if(action =="update"){
	updateSinglePage(singlePageId);
}