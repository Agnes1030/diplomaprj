var $updatePasswordForm = $("#update-password-form");
$(function () {
    $("#update-password .btn-save").click(function () {
        var form = $updatePasswordForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            $.post(ctx + "admin/user/updatePassword", $updatePasswordForm.serialize(), function (r) {
                if (r.code === 0) {
                    validateUpdatePassword.resetForm();
                    $MB.closeAndRestModal("update-password");
                    $MB.n_success(r.msg);
                    setTimeout(function () {
                        location.href = ctx + "logout";
                    },2000);
                } else $MB.n_danger(r.msg);
            });
        }
    });

    $("#update-password .btn-close").click(function () {
        $MB.closeAndRestModal("update-password");
    });

});