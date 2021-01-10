var $productCommentAddForm = $("#productComment-add-form");
$(function () {
    var options = {
    		single: true,
            width: '100%'
        };
    $("#productComment-add .btn-save").click(function () {
        var name = $(this).attr("name");
        var form = $productCommentAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/productComment/add", $productCommentAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.refreshTable("productCommentTable");
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/productComment/update", $productCommentAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.refreshTable("productCommentTable");
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#productComment-add .btn-close").click(function () {
        closeModal();
    });

});
function closeModal() {
    $("#productComment-add-button").attr("name", "save");
    $("#productComment-add-modal-title").html('新增评论');
    $MB.closeAndRestModal("productComment-add");
}