function updateProduct() {
    $.post(ctx + "admin/product/getProduct", {"productId": productId}, function (r) {
        if (r.code === 0) {
            var $form = $('#product-add-form');
            var $categoryTree = $('#categoryTree');
            var product = r.msg;
            $form.find("input[name='title']").val(product.title);
            $form.find("input[name='slug']").val(product.slug);
            $form.find("input[name='id']").val(product.id);
            $form.find("input[name='oldtitle']").val(product.title);
            $form.find("input[name='template']").val(product.template);
            $form.find("input[name='productId']").val(product.productId);
            $form.find("input[name='keywords']").val(product.keywords);
            $form.find("input[name='seqNum']").val(product.seqNum);
            $form.find("input[name='price']").val(product.price);
            $form.find("input[name='originPrice']").val(product.originPrice);
            $form.find("input[name='limitedPrice']").val(product.limitedPrice);
            $form.find("input[name='limitedTime']").val(product.limitedTime);
            setThumbnail(product.thumbnail);
            $form.find("textarea[name='description']").val(product.description);
            $form.find("textarea[name='summary']").val(product.summary);
            $form.find("textarea[name='usp']").val(product.usp);  
            $form.find("textarea[name='remarks']").val(product.remarks);       
            $form.find("textarea[name='content']").val(product.content);
            $form.find("input[name='tags']").val(product.tags);
            CKEDITOR.instances.productEditor.setData(product.content);
            $(":radio[name='status'][value='" + product.status + "']").prop("checked", "checked");
            $(":radio[name='status'][value='" + product.commentStatus + "']").prop("checked", "checked");
            $categoryTree.jstree().open_all();
            $categoryTree.jstree('select_node', product.categoryIds.split(","), true);
            $("#categoryIds").val(product.categoryIds);
            // 输出图片列表
            var productImages=product.productImages;
            if(productImages !=null && typeof(productImages)!="undefined"){
            	
            	for(var i =0;i<productImages.length;i++){
            		var imgUrl = productImages[i].src;
            		addProductImage(imgUrl);
            	}
            }
            $("#product-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}
function setThumbnail(thumbnail){
	var $form = $('#product-add-form');
    $form.find("input[name='thumbnail']").val(thumbnail);
    $form.find("input[name='file']").hide();
    $form.find('.control-group').find('#target').attr('src',thumbnail).show();
    $('.reupload,#uploadNext').show();//第一次上传完成，显示重新上传
}
