function updateCategory() {
    var selected = $("#categoryTable").bootstrapTable("getSelections");
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的分类！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个分类！');
        return;
    }
    var categoryId = selected[0].id;
    $.post(ctx + "admin/category/getCategory", {"categoryId": categoryId}, function (r) {
        if (r.code === 0) {
            var $form = $('#category-add');
            var $categoryTree = $('#categoryTree');
            $form.modal();
            var category = r.msg;
            $("#category-add-modal-title").html('修改分类');
            $form.find("input[name='name']").val(category.name);
            $form.find("input[name='slug']").val(category.slug);
            $form.find("input[name='oldCategoryName']").val(category.name);
            $form.find("input[name='id']").val(category.id);
            $form.find("input[name='seqNum']").val(category.seqNum);
            $form.find("input[name='keywords']").val(category.keywords);
            $form.find("textarea[name='description']").val(category.description);
            $form.find("input[name='template']").val(category.template);
            $(":radio[name='status'][value='" + category.status + "']").prop("checked", "checked");
            $(":radio[name='navShow'][value='" + category.navShow + "']").prop("checked", "checked");
            $categoryTree.jstree('select_node', category.parentId, true);
            $categoryTree.jstree('disable_node', category.id);
            $("#category-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}