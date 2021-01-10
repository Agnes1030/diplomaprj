var $registForm=$("#registForm");
var validator;
function validateRule() {
    var icon = "<i class='zmdi zmdi-close-circle zmdi-hc-fw'></i> ";
    validator = $registForm.validate({
        rules: {
        	email: {
            	required: true
            },
            username:{
            	required:true
            },
            imageCode:{
            	required:true
            }
        },
        messages: {
        	email: icon + "请输入邮件",
        	username: icon + "请输入用户名",
        	imageCode:icon +"请输入验证码"
        }
    });
}
validateRule();
function doRegist() {
    var validator = $registForm.validate();
    var flag = validator.form();
    var password = $("#password").val();
    var confirmPassword =$("#confirmPassword").val();
    if (confirmPassword !== password) {
        $MB.n_warning("两次密码输入不一致！");
        return;
    }
    if(flag){
        $.post(ctx + "auth/doRegist", $registForm.serialize(), function (r) {
            if (r.code === 0) {
                var msg = r.msg;
                if (r.code === 0) {
                    $MB.n_success("注册成功，2秒后跳转到登录页！");
                    setTimeout(function () {
                        window.location.href = ctx + "member/login";
                    }, 2000);
                } else {
                    $MB.n_warning(msg);
                }
            } else $MB.n_danger(r.msg);
        });
    }
}