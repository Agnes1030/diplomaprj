function editArticle(articleId) {
	 $("#article-add-button").attr("name","update");
    $.post(ctx + "admin/article/getArticle", {"articleId": articleId}, function (r) {
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
            $form.find("input[name='thumbnail']").val(article.thumbnail);
            $form.find("input[name='categoryIds']").val(article.categoryIds);
            $form.find('.control-group').find('#target').attr('src',article.thumbnail)
            $form.find("textarea[name='description']").val(article.description);
            $form.find("textarea[name='summary']").val(article.summary);
            $form.find("textarea[name='content']").val(article.content);
            $form.find("input[name='tags']").val(article.tags);
            $(":radio[name='status'][value='" + article.status + "']").prop("checked", "checked");
            $("#article-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}