var $singlePageAddForm = $("#singlePage-add-form");
$(function () {
	initEditor("singlePageEditor",470);
    $singlePageAddForm.find("input[name='template']").val("page.html");
    $("#singlePage-add .btn-save").click(function () {
        var name = $(this).attr("name");
        var form = $singlePageAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if(CKEDITOR.instances.singlePageEditor){
            	CKEDITOR.instances.singlePageEditor.updateElement();
            }
            if (name === "save") {
                $.post(ctx + "admin/singlePage/add", $singlePageAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/singlePage/update", $singlePageAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#singlePage-add .btn-close").click(function () {
        closeModal();
    });

});
/**
 * 初始化ckeditor 
 * @param elementId textArea 元素ID
 * @param height 编辑器高度 
 */
function initEditor(elementId,height){
	CKEDITOR.config.toolbar =
	    [
	        ['Bold', 'Italic', 'Underline', 'Strike', 'RemoveFormat'],
	        ['Blockquote', 'CodeSnippet', 'Image', 'Html5audio', 'Html5video', 'Flash', 'Table', 'HorizontalRule'],
	        ['Link', 'Unlink', 'Anchor'],
	        ['Outdent', 'Indent'],
	        ['NumberedList', 'BulletedList'],
	        ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
	        '/',
	        ['Format', 'FontSize'],
	        ['TextColor', 'BGColor'],
	        ['Undo', 'Redo'],
	        ['Maximize', 'Source']
	    ];

	CKEDITOR.config.wordcount = {
	    showCharCount: true,
	};

	CKEDITOR.config.disallowedContent = 'img{width,height};img[width,height]';
	CKEDITOR.addCss('.cke_editable img{max-width: 95%;}');

	var ed = CKEDITOR.replace(elementId, {
	    autoUpdateElement: true,
	    removePlugins: 'easyimage,cloudservices',
	    extraPlugins: 'entities,codesnippet,uploadimage,flash,image,wordcount,notification,html5audio,html5video,widget,widgetselection,clipboard,lineutils',
	    codeSnippet_theme: 'monokai_sublime',
	    height: height,
	    width:'100%',
	    uploadUrl:  '/cms/ckeditorUpload',
	    imageUploadUrl:'/cms/ckeditorUpload',
	    filebrowserUploadUrl:'/cms/ckeditorUpload',
	    //filebrowserBrowseUrl: '/admin/attachment/browse',
	    language: 'zh-cn'
	});	
    ed.on('instanceReady', function () {
        ed.setKeystroke(CKEDITOR.ALT.CTRL + 83, 'save'); //  Ctrl+s
        ed.setKeystroke(1114195, 'save'); // mac command +s
        // 扩展CKEditor的 ctrl + s 保存命令,方便全屏编辑时快捷保存
        ed.addCommand('save', {
            exec: function () {
                var ds = window.doSubmit;
                ds && ds();
            }
        });
    });
	return ed;
}
function closeModal() {
    $("#singlePage-add-button").attr("name", "save");
    $singlePageAddForm.find("input[name='status']").prop("checked", true);
    $("#singlePage-add-modal-title").html('新增单页');
    $("#status").html('有效');
    $MB.closeAndRestModal("singlePage-add");
    CKEDITOR.instances.singlePageEditor.setData("");
    $singlePageAddForm.find("input[name='template']").val("page.html");

}
function closeTab(){
	var curTab = parent.$(parent.document).data('multitabs').$element.navPanelList.find('li a.active');
	parent.$(parent.document).data('multitabs').close(curTab);
}