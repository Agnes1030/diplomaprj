function updateNavMenu() {
    var selected = $("#navMenuTable").bootstrapTable("getSelections");
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的导航！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个导航！');
        return;
    }
    var navMenuId = selected[0].id;
    $.post(ctx + "admin/navMenu/getNavMenu", {"navMenuId": navMenuId}, function (r) {
        if (r.code === 0) {
            var $form = $('#navMenu-add');
            var $navMenuTree = $('#navMenuTree');
            $form.modal();
            var navMenu = r.msg;
            $("#navMenu-add-modal-title").html('修改导航');
            $form.find("input[name='name']").val(navMenu.name);
            $form.find("input[name='oldNavMenuName']").val(navMenu.name);
            $form.find("input[name='id']").val(navMenu.id);
            $form.find("input[name='seqNum']").val(navMenu.seqNum);
            $form.find("input[name='target']").val(navMenu.target);
            $form.find("input[name='navUrl']").val(navMenu.navUrl);
            $form.find("select[name='target']").val(navMenu.target);
            $form.find("select[name='navType']").val(navMenu.navType);
            $('select').selectpicker('refresh');
            $(":radio[name='status'][value='" + navMenu.status + "']").prop("checked", "checked");
            $navMenuTree.jstree('select_node', navMenu.parentId, true);
            $navMenuTree.jstree('disable_node', navMenu.id);
            $("#navMenu-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}