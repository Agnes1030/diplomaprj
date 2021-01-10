var $topicAddForm = $("#topic-add-form");
$(function () {
    $topicAddForm.find("input[name='template']").val("productList.html");
    $("#topic-add .btn-save").click(function () {
        var name = $(this).attr("name");
        var form = $topicAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/topic/add", $topicAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("topicTable");
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/topic/update", $topicAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("topicTable");
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#topic-add .btn-close").click(function () {
        closeModal();
    });

});

function closeModal() {
    $("#topic-add-button").attr("name", "save");
    $topicAddForm.find("input[name='template']").val("productList.html");
    $topicAddForm.find("input[name='recommend']").prop("checked", true);
    $("#topic-add-modal-title").html('新增专题');
    $MB.closeAndRestModal("topic-add");

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