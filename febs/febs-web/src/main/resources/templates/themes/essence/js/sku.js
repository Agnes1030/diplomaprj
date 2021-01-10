  // 移除购物车
  $("#cartList").delegate(".product-remove","click",function(){
	  var specificationIds=$(this).siblings("input[name='specificationIds']").val();
	  var cartItem = {};
	  cartItem["quantity"]=1;
	  cartItem["specificationIds"]=specificationIds;
	  $Product.removeCart(cartItem);
  })
  // 生成订单
  $("#generateOrder").click(function(evt){
	  evt.preventDefault();
	  $Product.generateOrder();
  })