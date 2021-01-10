var $Product=(function(){
	// 刷新购物车
	function _refreshCart(){
      $.getJSON(ctx+"shopcart/cartList",function(res){
    	  if(res.code=='0'){
    		  var cartData = res.msg;
    		  $('#cartList').html($('#cartListTemplate').tmpl(cartData.items))
    		  $("#productMoney").text(cartData.productMoney);
    		  $('#totalMoney').text(cartData.total);
    		  $('#deliveryFee').text(cartData.deliveryFee);
    		  $(".cartListSize").text(cartData.items.length);
    	  }
	  });
	}
	// 添加购物车
	function _addToCart(productId,productName,specIds,imgUrl){
	 var cartItem={productId:productId,productName:productName,specIds:specIds,imgUrl:imgUrl};
     $.post(ctx+"shopcart/addCart",cartItem,function(result){
    	 if(result.code==0){
    		 // 展开购物车侧边栏
    		 $('#essenceCartBtn').trigger("click");
    		 _refreshCart();    		 
    	 }else{
    		 alert(result.msg);
    	 }
     });	
	}
	// 删除购物车
	function _removeCart(cartItem){
	     $.post(ctx+"shopcart/removeCart",cartItem,function(result){
	    	 _refreshCart();
	     });	
	}
	// 生成订单
    function _generateOrder(){
    	$.post(ctx+"member/generateOrder",function(result){
    		if(result.code=='0'){
    			var order = result.msg;
    			window.location.href=ctx+"member/viewOrder?orderId="+order.id;
    		}else{
    			alert(result.msg);
    		}
    	});    	
    }
    // 添加收藏商品
    function _addFavoriteProduct(productId){
    	$.post(ctx+"member/favourites/addProductFavourite",{productId:productId},function(result){
    		if(result.code=='0'){
    			var order = result.msg;
    		}else{
    			alert(result.msg);
    		}
    	}); 
    }
    function _addFavoriteArticle(articleId){
    	$.post(ctx+"member/favourites/addArticleFavourite",{articleId:articleId},function(result){
    		if(result.code=='0'){
    			var order = result.msg;
    		}else{
    			alert(result.msg);
    		}
    	}); 
    }
    function _notify(message, type) {
          alert(message);
    }     
	return {
		refreshCart:function(){
			_refreshCart();
		},
		addFavoriteProduct:function(productId){
			_addFavoriteProduct(productId);
		},
		addFavoriteArticle:function(articleId){
			_addFavoriteArticle(articleId);
		},
		addToCart:function(productId,productName,specIds,imgUrl){
			_addToCart(productId,productName,specIds,imgUrl);
		},
		removeCart:function(cartItem){
			_removeCart(cartItem);
		},
		generateOrder(){
			_generateOrder();
		},
        n_default: function (message) {
            _notify(message, "inverse");
        },
        n_info: function (message) {
            _notify(message, "info");
        },
        n_success: function (message) {
            _notify(message, "success");
        },
        n_warning: function (message) {
            _notify(message, "warning");
        },
        n_danger: function (message) {
            _notify(message, "danger");
        }		
	}
})($); 