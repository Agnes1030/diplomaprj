var $articleTagAddForm = $("#articleTag-add-form");
$(function () {
    $("#articleTag-add .btn-save").click(function () {
        var name = $(this).attr("name");
        var form = $articleTagAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/articleTag/add", $articleTagAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("articleTagTable");
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/articleTag/update", $articleTagAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("articleTagTable");
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#articleTag-add .btn-close").click(function () {
        closeModal();
    });

});

function closeModal() {
    $("#articleTag-add-button").attr("name", "save");
    $("#articleTag-add-modal-title").html('新增标签');
    $MB.closeAndRestModal("articleTag-add");
}
