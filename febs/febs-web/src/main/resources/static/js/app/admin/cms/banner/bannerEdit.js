function updateBanner() {
    var selected = $("#bannerTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的轮播图！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个轮播图！');
        return;
    }
    var bannerId = selected[0].id;
    $.get(ctx + "admin/banner/getBanner", {"bannerId": bannerId}, function (r) {
        if (r.code === 0) {
            var $form = $('#banner-add');
            $form.modal();
            var banner = r.msg;
            $("#banner-add-modal-title").html('修改轮播图');
            $form.find("input[name='title']").val(banner.title);
            $form.find("input[name='id']").val(banner.id);
            $form.find("input[name='seqNum']").val(banner.seqNum);
            $form.find("input[name='openUrl']").val(banner.openUrl);
            $form.find("input[name='imgPath']").val(banner.imgPath);
            $form.find("textarea[name='description']").val(banner.description);
            $form.find("textarea[name='content']").val(banner.content);
            $form.find("select[name='openTarget']").val(banner.openTarget);
            $(":radio[name='status'][value='" + banner.status + "']").prop("checked", "checked");
            $("#banner-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}