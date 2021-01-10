var $couponAddForm = $("#coupon-add-form");
$(function () {
    $couponAddForm.find("input[name='template']").val("couponProductList.html");
    $("#coupon-add .btn-save").click(function () {
        var name = $(this).attr("name");
        var form = $couponAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/coupon/add", $couponAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("couponTable");
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/coupon/update", $couponAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("couponTable");
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#coupon-add .btn-close").click(function () {
        closeModal();
    });

});

function closeModal() {
    $("#coupon-add-button").attr("name", "save");
    $couponAddForm.find("input[name='template']").val("couponProductList.html");
    $couponAddForm.find("input[name='recommend']").prop("checked", true);
    $("#coupon-add-modal-title").html('新增优惠券类型');
    $MB.closeAndRestModal("coupon-add");

}
$('#uploadTopic').fileupload({
    autoUpload: true,//自动上传
    url: "/cms/filesUpload/thumbnail",//ַ上传图片对应的地址
    dataType: 'json',
    done: function (e, data) {
        var oimage = data;
        $("#thumbnail").val(oimage.result.msg.src);
      }
})