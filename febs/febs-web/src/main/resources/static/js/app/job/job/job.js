$(function () {
    var settings = {
        url: ctx + "admin/job/list",
        pageSize: 10,
        toolbar:'#toolbar',
        queryParams: function (params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1
            };
        },
        columns: [{
            checkbox: true
        },
            {
                field: 'jobId',
                title: '任务ID'
            }, {
                field: 'beanName',
                title: 'Bean名称'
            }, {
                field: 'methodName',
                title: '方法名称'
            }, {
                field: 'params',
                title: '参数'
            }, {
                field: 'cronExpression',
                title: 'cron表达式'
            }, {
                field: 'remark',
                title: '备注'
            }, {
                field: 'status',
                title: '状态',
                formatter: function (value, row, index) {
					var value="";
					if (row.status == '0') {
						value = '<span class="badge badge-danger">禁用</span>';
					} else if(row.status == '1') {
						value = '<span class="badge badge-success">正常</span>';
					}else {
						value = row.pType ;
					}
					return value;
                }
            }
        ]
    };

    $MB.initTable('jobTable', settings);
    initSysCronClazzList();
});

function search() {
    $MB.refreshTable('jobTable');
}

function refresh() {
    search();
}

function deleteJob() {
    var selected = $("#jobTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的任务！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].jobId;
        if (i !== (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "确定删除选中的任务？",
        confirmButtonText: "确定删除"
    }, function () {
        $.post(ctx + 'admin/job/delete', {"ids": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function runJob() {
    var selected = $("#jobTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要立即执行的任务！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].jobId;
        if (i !== (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "确定执行选中的任务？",
        confirmButtonText: "确定执行"
    }, function () {
        $.post(ctx + 'admin/job/run', {"jobIds": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function pauseJob() {
    var selected = $("#jobTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要暂停的任务！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].jobId;
        if (i !== (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "确定暂停选中的任务？",
        confirmButtonText: "确定暂停"
    }, function () {
        $.post(ctx + 'admin/job/pause', {"jobIds": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function resumeJob() {
    var selected = $("#jobTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要恢复的任务！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].jobId;
        if (i !== (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "确定恢复选中的任务？",
        confirmButtonText: "确定恢复"
    }, function () {
        $.post(ctx + 'admin/job/resume', {"jobIds": ids}, function (r) {
            if (r.code === 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportJobExcel() {
    $.post(ctx + "admin/job/excel", $(".job-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function exportJobCsv() {
    $.post(ctx + "admin/job/csv", $(".job-table-form").serialize(), function (r) {
        if (r.code === 0) {
            window.location.href = "file/download?fileName=" + r.msg + "&delete=" + true;
        } else {
            $MB.n_warning(r.msg);
        }
    });
}

function initSysCronClazzList() {
    $.getJSON(ctx + "admin/job/getSysCronClazz", function (r) {
        r = r.code === 0 ? r.msg : [];
        $('#sys-cron-clazz-list-bean').autocomplete({
            hints: r,
            keyname: 'beanName',
            width: 70,
            // height: 32,
            valuename: 'beanName',
            showButton: false,
            onSubmit: function (text) {
                $('#sys-cron-clazz-list-bean').siblings("input[name='beanName']").val(text);

            }
        });
        $('#sys-cron-clazz-list-method').autocomplete({
            hints: r,
            keyname: 'beanName',
            width: 70,
            // height: 31,
            valuename: 'methodName',
            showButton: false,
            onSubmit: function (text) {
                $('#sys-cron-clazz-list-method').siblings("input[name='methodName']").val(text);
            }
        });
    });


}
