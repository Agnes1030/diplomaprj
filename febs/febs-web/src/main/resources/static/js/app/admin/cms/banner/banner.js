$(function () {
    var $bannerTableForm = $(".banner-table-form");
    var settings = {
        url: ctx + "admin/banner/list",
        pagination:false,
        toolbar:'#toolbar',
        queryParams: {},
        columns: [{
            checkbox: true
        }, {
        	title: 'ID',
            field: 'id',
            visible: true
        },{
            field: 'title',
            title: '标题'
        }, {
            field: 'imgPath',
            title: '图片',
            formatter:function(item,index){
            	return '<img style="width:200px;height:100px;" src='+item+'  onerror="this.onerror=\'\';this.src=\'/img/default.png\'"></img>';
            }
        }, {
            field: 'description',
            title: '描述'
        }, {
            field: 'openUrl',
            title: '打开链接'
        },{
            title: '打开方式',
            field: 'openTarget',
            formatter: function (item, index) {
                if (item == '_self') return '<font class="text-success">当前页</font>';
                if (item == '_blank') return '<font class="text-success">新打开</font>';
            }
        },{
            field: 'createTime',
            title: '创建时间'
        }, {
            field: 'status',
            title: '状态',
            formatter: function (value, row, index) {
                if (value === '1') return '<font class="text-success">有效</font>';
                if (value === '0') return '<font class="text-danger">锁定</font>';
            }
        }

        ]
    };

    $MB.initTable('bannerTable', settings);
});

function search() {
    $MB.refreshTable('bannerTable');
}

function refresh() {
    $MB.refreshTable('bannerTable');
}

function deleteBanners() {
    var selected = $("#bannerTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的轮播图！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].id;
        if (i !== (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "确定删除选中轮播图？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/banner/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportBannerExcel() {
    $.post(ctx + "admin/banner/excel", $(".banner-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportBannerCsv() {
    $.post(ctx + "admin/banner/csv", $(".banner-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}