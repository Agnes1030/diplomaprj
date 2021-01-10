var $friendLinkAddForm = $("#friendLink-add-form");
var $friendLinkSelect = $friendLinkAddForm.find("select[name='target']");
$(function () {
    var options = {
    		single: true,
            width: '100%'
        };
    imgUpLoad("file");
    imgUpLoad("uploadNext");
    $("#friendLink-add .btn-save").click(function () {
        var name = $(this).attr("name");
        var form = $friendLinkAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/friendLink/add", $friendLinkAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("friendLinkTable");
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/friendLink/update", $friendLinkAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("friendLinkTable");
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#friendLink-add .btn-close").click(function () {
        closeModal();
    });

});

function closeModal() {
    $("#friendLink-add-button").attr("name", "save");
    $("#friendLink-add-modal-title").html('新增友情链接');
    $MB.closeAndRestModal("friendLink-add");
}
function imgUpLoad(eleId) {
    $('#' + eleId).fileupload({
        autoUpload: true,//自动上传
        url: "/cms/filesUpload/thumbnail",//ַ上传图片对应的地址
        dataType: 'json',
        done: function (e, data) {
            var oimage = data, _this = $('#' + eleId);
            if (eleId == 'file') {
                _this.hide();
                _this.siblings('img').attr('src', oimage.result.msg.src).show();
                $('.reupload').show();//第一次上传完成，显示重新上传
            } else {//重新上传
                _this.closest('.control-group').find('#target').attr('src',oimage.result.msg.src);    
            }
            _this.closest('.control-group').find(".filevalue").val(oimage.result.msg.src); 
          }
  })
}   