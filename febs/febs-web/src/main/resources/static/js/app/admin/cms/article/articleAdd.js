var $articleAddForm = $("#article-add-form");
$(function () {
	initEditor("articleEditor",470);
    createCategoryTree();
    imgUpLoad("file");
    imgUpLoad("uploadNext");
    $articleAddForm.find("input[name='template']").val("article.html");
    $("#article-add .btn-save").click(function () {
        var name = $(this).attr("name");
        getCategory();
        var form = $articleAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if(CKEDITOR.instances.articleEditor){
            	CKEDITOR.instances.articleEditor.updateElement();
            }
            if (name === "save") {
                $.post(ctx + "admin/article/add", $articleAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        $MB.n_success(r.msg);
                       // closeTab();
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/article/update", $articleAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        $MB.n_success(r.msg);
                       // $MB.refreshTable("articleTable");
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

});
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
            } else {
            	//重新上传
                _this.closest('.control-group').find('#target').attr('src',oimage.result.msg.src);    
            }
            _this.closest('.control-group').find(".filevalue").val(oimage.result.msg.src); 
          }
  })
}  
function closeTab(){
	var curTab = parent.$(parent.document).data('multitabs').$element.navPanelList.find('li a.active');
	parent.$(parent.document).data('multitabs').close(curTab);
}
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
function createCategoryTree() {
    $.post(ctx + "admin/category/tree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#categoryTree').jstree({
                "core": {
                    'data': data.children,
                    'multiple': true
                },
                "state": {
                    "disabled": false,
                    "opened":true
                },
                "checkbox": {
                    "three_state": false // 取消选择父节点后选中所有子节点
                },
                "plugins": ["wholerow", "checkbox"]
            }).on('ready.jstree', function(event, obj) {
	            $('#categoryTree').jstree().open_all();
				if(action=='update'){
					// 方法在articleEdit.js文件中
					updateArticle(articleId);
				}
             });

        } else {
            $MB.n_danger(r.msg);
        }
    })
}
function getCategory() {
    var ref = $('#categoryTree').jstree(true);
    var ids = ref.get_selected().join(",");
    $("[name='categoryIds']").val(ids);
}