
function updateArticle(articleId) {

    if (articleId ==null ) {
        $MB.n_warning('请选择要修改的文章！');
        return;
    }
    $.get(ctx + "admin/article/getArticle", {"articleId": articleId}, function (r) {
        if (r.code === 0) {
            var $form = $('#article-add');
            var $categoryTree = $('#categoryTree');
            var article = r.msg;
            $("#article-add-modal-title").html('修改文章');
            $form.find("input[name='title']").val(article.title);
            $form.find("input[name='slug']").val(article.slug);
            $form.find("input[name='id']").val(article.id);
            $form.find("input[name='oldtitle']").val(article.title);
            $form.find("input[name='template']").val(article.template);
            $form.find("input[name='articleId']").val(article.articleId);
            $form.find("input[name='keywords']").val(article.keywords);
            $form.find("input[name='seqNum']").val(article.seqNum);
            setThumbnail(article.thumbnail);
            $form.find("textarea[name='description']").val(article.description);
            $form.find("textarea[name='summary']").val(article.summary);
            $form.find("textarea[name='content']").val(article.content);
            $form.find("input[name='tags']").val(article.tags);
            CKEDITOR.instances.articleEditor.setData(article.content);
            $(":radio[name='status'][value='" + article.status + "']").prop("checked", "checked");
            $("#article-add-button").attr("name", "update");
            $categoryTree.jstree('select_node', article.categoryIds.split(","), true);
        } else {
            $MB.n_danger(r.msg);
        }
    });
}
function setThumbnail(thumbnail){
	var $form = $('#article-add');
    $form.find("input[name='thumbnail']").val(thumbnail);
    $form.find("input[name='file']").hide();
    $form.find('.control-group').find('#target').attr('src',thumbnail).show();
    $('.reupload,#uploadNext').show();//第一次上传完成，显示重新上传
}