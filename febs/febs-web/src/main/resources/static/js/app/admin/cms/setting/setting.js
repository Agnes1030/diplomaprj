var $settingForm = $('#setting-add-form');
var $themeSelect = $settingForm.find("select[name='theme']");
$(function(){
    $.get(ctx + "admin/setting/list", function (r) {
        if (r.code === 0) {
            var rows = r.msg;
            for(var i=0;i<rows.length;i++){
            	var row = rows[i];
            	$settingForm.find("input[name='"+row.key+"'],textarea[name='"+row.key+"'],select[name='"+row.key+"']").val(row.value);
            }
            $('select').selectpicker('refresh');
        } else {
            $MB.n_danger(r.msg);
        }
    })
        $('#uploadLogo').fileupload({
        autoUpload: true,//自动上传
        url: "/cms/filesUpload/logo",//ַ上传图片对应的地址
        dataType: 'json',
        done: function (e, data) {
            var oimage = data;
            $("#site_logo").val(oimage.result.msg.src);
          }
  })
    $("#setting-add-button").click(function(){
        $.post(ctx + "admin/setting/update", $settingForm.serialize(), function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
            } else $MB.n_danger(r.msg);
        });
    })
      
})
function closeTab(){
	var curTab = parent.$(parent.document).data('multitabs').$element.navPanelList.find('li a.active');
	parent.$(parent.document).data('multitabs').close(curTab);
}