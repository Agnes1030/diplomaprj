var $productAddForm = $("#product-add-form");
var viewerObj;
$(function () {
	initEditor("productEditor",470);
    createCategoryTree();
    imgUpLoad("file");
    imgUpLoad("uploadNext");
    imgProductUpLoad("productFile");
    imgSkuUpLoad("skuFile");
    $productAddForm.find("input[name='template']").val("product.html");
    $("#product-add-button").click(function () {
        getCategory();
        var form = $productAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if(CKEDITOR.instances.productEditor){
            	CKEDITOR.instances.productEditor.updateElement();
            }
            setSku();
            if (operation === "save") {
                $.post(ctx + "admin/product/add", $productAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
            if (operation === "update") {
                $.post(ctx + "admin/product/update", $productAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#product-add .btn-close").click(function () {
        closeModal();
    });

});
function setSku(){
	var specArr=[];
	$(".spec").each(function(index,tr){
		var obj={};
		var specName = $(tr).find(".specName").text();
		var specValue = $(tr).find(".specValue").text();
		obj['specification']=specName;
		obj['value']=specValue;
		specArr.push(obj)
	})
	var specStr = JSON.stringify(specArr)
	$("#specStr").val(specStr);
	//console.log(specStr);
	var skuArr=[];
	$(".sku").each(function(index,tr){
		var obj={};
		var sku_spec=$(tr).find(".sku_spec").text();
		var sku_price=$(tr).find(".sku_price").text();
		var sku_oripice=$(tr).find(".sku_oripice").text();
		var sku_img=$(tr).find(".skuImgUrl").val();
		var sku_stock=$(tr).find(".sku_stock").text();
		obj['specification']=sku_spec;
		obj['price']=sku_price;
		obj['originPrice']=sku_oripice;
		obj['stock']=sku_stock;
		obj['imgUrl']=sku_img;
		skuArr.push(obj);
	})
	var skuStr = JSON.stringify(skuArr);
	$("#skuStr").val(skuStr);
}
function addProductImage(imgUrl){
	 var upEle=$("#upElement");
     $('<li class="col-xs-4 col-sm-3 col-md-3 imgObj"><figure><input type="hidden" value="'+imgUrl+'" name="productImgs"/><img src="'+imgUrl+'" ><figcaption><a class="btn btn-round btn-square btn-primary" href="#!"><i class="mdi mdi-eye"></i></a><a class="btn btn-round btn-square btn-danger" href="#!"><i class="mdi mdi-delete"></i></a></figcaption></figure></li>').insertBefore(upEle);
     // 预览图片刷新，否则新增加图片无效
     viewerObj.update();
}
function previewImage(){
	var $viewer = $('#productsLi');
	// 图片预览
	$viewer.viewer();  
	viewerObj= $viewer.data('viewer');
}
function imgProductUpLoad(eleId) {
    $('#' + eleId).fileupload({
        autoUpload: true,//自动上传
        url: "/admin/product/filesUpload/thumbnail",//ַ上传图片对应的地址
        dataType: 'json',
        done: function (e, data) {
            var oimage = data, _this = $('#' + eleId);
            var imgUrl =oimage.result.msg.src;
            addProductImage(imgUrl);
          }
  })
}
// sku图片
function imgSkuUpLoad(eleId){
    $('#' + eleId).fileupload({
        autoUpload: true,//自动上传
        url: "/admin/product/filesUpload/thumbnail",//ַ上传图片对应的地址
        dataType: 'json',
        done: function (e, data) {
            var oimage = data, _this = $('#' + eleId);
            var imgUrl =oimage.result.msg.src;
            $("#skuImgUrl").val(imgUrl);
            var upEle=$("#skuUpElement");
            $('<li class="col-xs-4 col-sm-3 col-md-3 imgObj"><figure><img src="'+imgUrl+'" ><figcaption><a class="btn btn-round btn-square btn-primary" href="#!"><i class="mdi mdi-eye"></i></a><a class="btn btn-round btn-square btn-danger" href="#!"><i class="mdi mdi-delete"></i></a></figcaption></figure></li>').insertBefore(upEle);
             upEle.hide(); 
        }
  })	
}
// 删除元素
$("#productsLi").delegate(".mdi-delete","click",function(evt){
	var liEle=$(evt.currentTarget).closest("li");
	liEle.remove();
})
$("#skuLi").delegate(".mdi-delete","click",function(evt){
	var liEle=$(evt.currentTarget).closest("li");
	liEle.remove();
	$("#skuUpElement").show();
})

// 图片预览
$("#productsLi").delegate(".mdi-eye","click",function(evt){
	var liEle=$(evt.currentTarget).closest("li");
	var imgEle=liEle.find("img");
	imgEle.trigger("click");
})
function imgUpLoad(eleId) {
    $('#' + eleId).fileupload({
        autoUpload: true,//自动上传
        url: "/admin/product/filesUpload/thumbnail",//ַ上传图片对应的地址
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
	    uploadUrl:  '/shop/ckeditorUpload',
	    imageUploadUrl:'/shop/ckeditorUpload',
	    filebrowserUploadUrl:'/shop/ckeditorUpload',
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
        previewImage();
        if(operation==="update"){
        	updateProduct();
        }
    });
	return ed;
}
function closeModal() {
    $("#product-add-button").attr("name", "save");
    $MB.resetJsTree("categoryTree");
    CKEDITOR.instances.productEditor.setData("");
    $("#productsLi .imgObj").remove();
    $productAddForm.find("input[name='template']").val("product.html");
    window.location.href="/admin/product"
}
// 规格删除按钮事件代理
$("table").delegate(".delBtn","click",function(evt){
	var _self = this;
    $MB.confirm({
        text: "确定删除？",
        confirmButtonText: "确定删除"
    }, function () {
    	$(_self).closest("tr").remove();
    });
	
})
function createCategoryTree() {
    $.post(ctx + "admin/productCategory/tree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#categoryTree').jstree({
                "core": {
                    'data': data.children,
                    'multiple': true
                },
                "state": {
                    "disabled": true
                },
                "checkbox": {
                    "three_state": false // 取消选择父节点后选中所有子节点
                },
                "plugins": ["wholerow", "checkbox"]
            });
        } else {
            $MB.n_danger(r.msg);
        }
    })
}
function closeTab(){
	var curTab = parent.$(parent.document).data('multitabs').$element.navPanelList.find('li a.active');
	parent.$(parent.document).data('multitabs').close(curTab);
}
function getCategory() {
    var ref = $('#categoryTree').jstree(true);
    var ids = ref.get_selected().join(",");
    $("[name='categoryIds']").val(ids);
}
function getSelect(label,arrs){
	var skuRowStart='<div class="form-group"><label for="recipient-name" class="control-label">'+label+':</label><select  name="'+label+'" class="form-control">';
    var optStr="";
    for(var i=0;i<arrs.length;i++){
    	var arrv=arrs[i];
    	optStr+='<option value="'+arrv+'">'+arrv+'</option>';
    }
    var skuRowEnd='</select></div>';	
    var selectStr=skuRowStart+optStr+skuRowEnd;
    return selectStr;
}
function refreSpec(){
	$("#specSelect").html("");
    $(".spec").each(function(index,ele){
    	var specName =$(ele).find(".specName").text();
    	var specValue =$(ele).find(".specValue").text();
    	var selectOpt=getSelect(specName,specValue.split(","));
    	$("#specSelect").append(selectOpt);
    })	
}
$("#addSkuBtn").click(function(){
	var specLen = $("#specBody").find(".spec").length;
	if(specLen == 0){
		$MB.n_danger("规格不能为空")
		return false;
	}
	refreSpec();
	$("#openSku").trigger("click");
})