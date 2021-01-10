var $articleCommentAddForm = $("#articleComment-add-form");
$(function () {
    var options = {
    		single: true,
            width: '100%'
        };
    $("#articleComment-add .btn-save").click(function () {
        var name = $(this).attr("name");
        var form = $articleCommentAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/articleComment/add", $articleCommentAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.refreshTable("articleCommentTable");
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/articleComment/update", $articleCommentAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.refreshTable("articleCommentTable");
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#articleComment-add .btn-close").click(function () {
        closeModal();
    });

});
function closeModal() {
    $("#articleComment-add-button").attr("name", "save");
    $("#articleComment-add-modal-title").html('新增评论');
    $MB.closeAndRestModal("articleComment-add");
}