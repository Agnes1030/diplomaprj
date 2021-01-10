var $categoryAddForm = $("#category-add-form");
$(function () {
    var options = {
    		single: true,
            width: '100%'
        };
    createCategoryTree();
    $categoryAddForm.find("input[name='template']").val("articleList.html");
    $("#category-add .btn-save").click(function () {
        var name = $(this).attr("name");
        getCategory();
        var form = $categoryAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/category/add", $categoryAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        refresh();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/category/update", $categoryAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        refresh();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#category-add .btn-close").click(function () {
        closeModal();
    });

});
function closeModal() {
    $("#category-add-button").attr("name", "save");
    $("#category-add-modal-title").html('新增分类');
    $MB.closeAndRestModal("category-add");
    $categoryAddForm.find("input[name='template']").val("articleList.html");
    $MB.resetJsTree("categoryTree");
}

function createCategoryTree() {
    $.get(ctx + "admin/category/tree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#categoryTree').jstree({
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
	            $('#categoryTree').jstree().open_all();
             });
        } else {
            $MB.n_danger(r.msg);
        }
    })

}

function getCategory() {
    var ref = $('#categoryTree').jstree(true);
    var categoryIds = ref.get_checked();
    $("[name='parentId']").val(categoryIds[0]);
}