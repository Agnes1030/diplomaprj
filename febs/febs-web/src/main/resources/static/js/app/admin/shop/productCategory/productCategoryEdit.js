function updateProductCategory() {
    var selected = $("#productCategoryTable").bootstrapTable("getSelections");
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的分类！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个分类！');
        return;
    }
    var productCategoryId = selected[0].id;
    $.post(ctx + "admin/productCategory/getProductCategory", {"productCategoryId": productCategoryId}, function (r) {
        if (r.code === 0) {
            var $form = $('#productCategory-add');
            var $productCategoryTree = $('#productCategoryTree');
            $form.modal();
            var productCategory = r.msg;
            $("#productCategory-add-modal-title").html('修改分类');
            $form.find("input[name='name']").val(productCategory.name);
            $form.find("input[name='slug']").val(productCategory.slug);
            $form.find("input[name='oldProductCategoryName']").val(productCategory.name);
            $form.find("input[name='id']").val(productCategory.id);
            $form.find("input[name='seqNum']").val(productCategory.seqNum);
            $form.find("input[name='keywords']").val(productCategory.keywords);
            $form.find("textarea[name='description']").val(productCategory.description);
            $form.find("input[name='template']").val(productCategory.template);
            $form.find("input[name='thumbnail']").val(productCategory.thumbnail);
            $(":radio[name='recommend'][value='" + productCategory.recommend + "']").prop("checked", "checked");
            $(":radio[name='status'][value='" + productCategory.status + "']").prop("checked", "checked");
            $(":radio[name='navShow'][value='" + productCategory.navShow + "']").prop("checked", "checked");
            $productCategoryTree.jstree('select_node', productCategory.parentId, true);
            $productCategoryTree.jstree('disable_node', productCategory.id);
            $("#productCategory-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}