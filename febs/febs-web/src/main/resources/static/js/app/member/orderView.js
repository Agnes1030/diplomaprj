$(function(){
	// 确认收货
	$("#sureGetProducts").click(function(){
		 $.confirm({
		        title: '确认收货',
		        content: '确认收到商品了么?',
		        type: 'green',
		        icon: 'glyphicon glyphicon-question-sign',
		        buttons: {
		            ok: {
		                text: '确认',
		                btnClass: 'btn-primary',
		                action: function() {
		            		$.getJSON(ctx+"member/sureGetProducts",{orderId:orderId},function(res){
		            			if(res.code==0){
		            				window.location.href=ctx+"member/viewOrder?orderId="+orderId;
		            			}
		            		});
		                }
		            },
		            cancel: {
		                text: '取消',
		                btnClass: 'btn-primary'
		            }
		        }
		    });
	})
	// 取消订单
	$("#cancelOrder").click(function(){
	    $.confirm({
	        title: '取消订单',
	        content: '' +
	        '<form action="" class="formName">' +
	        '<div class="form-group">' +
	        '<label>原因</label>' +
	        '<textarea placeholder="原因" class="orderSummary form-control" rows="4"></textarea>' +
	        '</div>' +
	        '</form>',
	        buttons: {
	            formSubmit: {
	                text: '提交',
	                btnClass: 'btn-blue',
	                action: function () {
	                    var orderSummary = this.$content.find('.orderSummary').val();
	        	    	var orderId=$("#orderId").val();
	        			$.getJSON(ctx+"member/cancelUserOrder",{orderId:orderId,userSummary:orderSummary},function(res){
	        				if(res.code==0){
	        					window.location.href=ctx+"member/viewOrder?orderId="+orderId;
	        				}
	        			});
	                }
	            },
	            cancel: {
	                text: '取消'
	            },
	        }
	    });
	});
	// 申请退款
	$("#applyRefundBtn").click(function(){
	    $.confirm({
	        title: '申请退款',
	        content: '' +
	        '<form action="" class="formName">' +
	        '<div class="form-group">' +
	        '<label>原因</label>' +
	        '<textarea placeholder="原因" class="orderSummary form-control" rows="4"></textarea>' +
	        '</div>' +
	        '</form>',
	        buttons: {
	            formSubmit: {
	                text: '提交',
	                btnClass: 'btn-blue',
	                action: function () {
	                    var orderSummary = this.$content.find('.orderSummary').val();
	        	    	var orderId=$("#orderId").val();
	        			$.getJSON(ctx+"member/applyRefund",{orderId:orderId,userSummary:orderSummary},function(res){
	        				if(res.code==0){
	        					window.location.href=ctx+"member/viewOrder?orderId="+orderId;
	        				}
	        			});
	                }
	            },
	            cancel: {
	                text: '取消'
	            },
	        }
	    });		
	});
})