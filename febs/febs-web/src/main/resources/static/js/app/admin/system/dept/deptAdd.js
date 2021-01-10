var $deptAddForm = $("#dept-add-form");
$(function () {
    createDeptTree();
    $("#dept-add .btn-save").click(function () {
        var name = $(this).attr("name");
        getDept();
        var form = $deptAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/dept/add", $deptAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        refresh();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/dept/update", $deptAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        refresh();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#dept-add .btn-close").click(function () {
        closeModal();
    });

});

function closeModal() {
    $("#dept-add-button").attr("name", "save");
    $("#dept-add-modal-title").html('新增部门');
    $MB.closeAndRestModal("dept-add");
    $MB.refreshJsTree("deptTree");
}

function createDeptTree() {
    $.post(ctx + "admin/dept/tree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#deptTree').jstree({
                "core": {
                    'data': data.children,
                    'multiple': false
                },
                "state": {
                    "disabled": true
                },
                "checkbox": {
                    "three_state": false
                },
                "plugins": ["wholerow", "checkbox"]
            }).on('ready.jstree', function(event, obj) {
	            $('#deptTree').jstree().open_all();
             });
        } else {
            $MB.n_danger(r.msg);
        }
    })

}

function getDept() {
    var ref = $('#deptTree').jstree(true);
    var deptIds = ref.get_checked();
    $("[name='parentId']").val(deptIds[0]);
}