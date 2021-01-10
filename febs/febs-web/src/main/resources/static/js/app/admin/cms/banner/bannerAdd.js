var $bannerAddForm = $("#banner-add-form");
$(function () {
    $("#banner-add .btn-save").click(function () {
        var name = $(this).attr("name");
        var form = $bannerAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/banner/add", $bannerAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("bannerTable");
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/banner/update", $bannerAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("bannerTable");
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#banner-add .btn-close").click(function () {
        closeModal();
    });

});

function closeModal() {
    $("#banner-add-button").attr("name", "save");
    $bannerAddForm.find("input[name='status']").prop("checked", true);
    $("#banner-add-modal-title").html('新增轮播图');
    $MB.closeAndRestModal("banner-add");

}
$('#uploadBanner').fileupload({
    autoUpload: true,//自动上传
    url: "/cms/filesUpload/banner",//ַ上传图片对应的地址
    dataType: 'json',
    done: function (e, data) {
        var oimage = data;
        $("#imgPath").val(oimage.result.msg.src);
      }
})