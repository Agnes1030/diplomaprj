var $productCategoryAddForm = $("#productCategory-add-form");
$(function () {
    var options = {
    		single: true,
            width: '100%'
        };
    createProductCategoryTree();
    $productCategoryAddForm.find("input[name='template']").val("productList.html");
    $("#productCategory-add .btn-save").click(function () {
        var name = $(this).attr("name");
        getProductCategory();
        var form = $productCategoryAddForm[0];
        var flag = form.checkValidity();
        form.classList.add('was-validated');
        if (flag) {
            if (name === "save") {
                $.post(ctx + "admin/productCategory/add", $productCategoryAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        refresh();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name === "update") {
                $.post(ctx + "admin/productCategory/update", $productCategoryAddForm.serialize(), function (r) {
                    if (r.code === 0) {
                        closeModal();
                        refresh();
                        $MB.n_success(r.msg);
                    } else $MB.n_danger(r.msg);
                });
            }
        }
    });

    $("#productCategory-add .btn-close").click(function () {
        closeModal();
    });

});
function closeModal() {
    $("#productCategory-add-button").attr("name", "save");
    $("#productCategory-add-modal-title").html('新增分类');
    $MB.closeAndRestModal("productCategory-add");
    $productCategoryAddForm.find("input[name='template']").val("productList.html");
    $MB.refreshJsTree("productCategoryTree", createProductCategoryTree());
}
function createProductCategoryTree() {
    $.get(ctx + "admin/productCategory/tree", {}, function (r) {
        if (r.code === 0) {
            var data = r.msg;
            $('#productCategoryTree').jstree({
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
            });
        } else {
            $MB.n_danger(r.msg);
        }
    })

}

function getProductCategory() {
    var ref = $('#productCategoryTree').jstree(true);
    var productCategoryIds = ref.get_checked();
    $("[name='parentId']").val(productCategoryIds[0]);
}
$('#uploadProCategory').fileupload({
    autoUpload: true,//自动上传
    url: "/cms/filesUpload/thumbnail",//ַ上传图片对应的地址
    dataType: 'json',
    done: function (e, data) {
        var oimage = data;
        $("#thumbnail").val(oimage.result.msg.src);
      }
})