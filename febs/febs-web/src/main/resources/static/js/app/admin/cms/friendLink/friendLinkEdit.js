function updateFriendLink() {
    var selected = $("#friendLinkTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的友情链接！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个友情链接！');
        return;
    }
    resetImg();
    var friendLinkId = selected[0].id;
    $.get(ctx + "admin/friendLink/getFriendLink", {"friendLinkId": friendLinkId}, function (r) {
        if (r.code === 0) {
            var $form = $('#friendLink-add');
            $form.modal();
            var friendLink = r.msg;
            $("#friendLink-add-modal-title").html('修改友情链接');
            $form.find("input[name='name']").val(friendLink.name);
            $form.find("input[name='id']").val(friendLink.id);
            $form.find("input[name='urlAddress']").val(friendLink.urlAddress);
            $form.find("input[name='seqNum']").val(friendLink.seqNum);
            $(":radio[name='status'][value='" + friendLink.status + "']").prop("checked", "checked");
            createImageSection(friendLink.thumbnail);
            $("#friendLink-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
      function createImageSection(thumbnail) {
        var imageBox  = $(".image-box");
        var imageSection = $("<section class='image-section image-loading'></section>");
        var imageShade   = $("<div class='image-shade'></div>");
        var imageShow    = $("<img class='image-show' src='" + thumbnail + "'/>");
        var imageInput   = $("<input class='" + thumbnail + "' name='" + thumbnail + "' value='' type='hidden'>");
        var imageZoom    = $("<div class='image-zoom'></div>");
        var imageDelete  = $("<div class='image-delete'></div>");

        imageBox.prepend(imageSection);

        imageShade.appendTo(imageSection);
        imageDelete.appendTo(imageSection);
        // 判断是否开启缩放功能
        imageZoom.appendTo(imageSection);

        imageShow.appendTo(imageSection);
        imageInput.appendTo(imageSection);
    };   
    function resetImg(){
    	var imageBox  = $(".image-box");
    	imageBox.find(".image-section").remove();
    }
}