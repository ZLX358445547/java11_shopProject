package com.neuedu.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.*;
import com.neuedu.pojo.*;
import com.neuedu.service.IOrderService;
import com.neuedu.service.IUserService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.TokenCache;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ShippingMapper shippingMapper;
    @Autowired
    PayInfoMapper payInfoMapper;
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //step1:参数非空校验

        if(shippingId==null){
            return ServerResponse.createServerResponseByError("地址参数不能为空");
        }

        //step2:根据userId查询购物车中已选中的商品 -》List<Cart>
        List<Cart> cartList= cartMapper.findCartListByUserIdAndChecked(userId);

        //step3:List<Cart>-->List<OrderItem>
        ServerResponse serverResponse= getCartOrderItem(userId,cartList);
        if(!serverResponse.isSuccess()){
            return  serverResponse;
        }

        //step4:创建订单order并将其保存到数据库
        //计算订单的价格
        BigDecimal orderTotalPrice=new BigDecimal("0");
        List<OrderItem> orderItemList=(List<OrderItem>)serverResponse.getData();
        if(orderItemList==null||orderItemList.size()==0){
            return  ServerResponse.createServerResponseByError("购物车为空");
        }
        orderTotalPrice=getOrderPrice(orderItemList);
        Order order=createOrder(userId,shippingId,orderTotalPrice);

        int a=3/0;

        if(order==null){
            return ServerResponse.createServerResponseByError("订单创建失败");
        }
        //step5:将List<OrderItem>保存到数据库
        for(OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //批量插入
        orderItemMapper.insertBatch(orderItemList);
        //step6:扣库存

        //step7：购物车中清空已经下单扣库存的产品
        //step8：OrderVO
        return null;
    }


    /*
    * step3:
    * 将List<Cart>转换为List<OrderItem>(订单明细)的方法
    * */
    private  ServerResponse getCartOrderItem(Integer userId,List<Cart> cartList){

        if(cartList==null||cartList.size()==0){
            return ServerResponse.createServerResponseByError("购物车空");
        }
        List<OrderItem> orderItemList= Lists.newArrayList();

        for(Cart cart:cartList){

            OrderItem orderItem=new OrderItem();
            orderItem.setUserId(userId);
            Product product=productMapper.selectByPrimaryKey(cart.getProductId());
            if(product==null){
                return  ServerResponse.createServerResponseByError("id为"+cart.getProductId()+"的商品不存在");
            }
            if(product.getStatus()!= Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){//商品下架
                return ServerResponse.createServerResponseByError("id为"+product.getId()+"的商品已经下架");
            }
            if(product.getStock()<cart.getQuantity()){//库存不足
                return ServerResponse.createServerResponseByError("id为"+product.getId()+"的商品库存不足");
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));

            orderItemList.add(orderItem);
        }

        return  ServerResponse.createServerResponseBySucess(orderItemList);
    }





    /**
     * step4：
     * （1）创建订单
     * （3）订单状态，需要定义枚举类型，OrderStatusEnum
     * （4）支付状态定义枚举类型
     *
     * */

    private Order createOrder(Integer userId, Integer shippingId, BigDecimal orderTotalPrice){
        Order order=new Order();
        order.setOrderNo(generateOrderNO());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        //订单状态
        order.setStatus(Const.OrderStatusEnum.ORDER_UN_PAY.getCode());
        //订单金额
        order.setPayment(orderTotalPrice);
        order.setPostage(0);
        order.setPaymentType(Const.PaymentEnum.ONLINE.getCode());

        //保存订单
        int result=orderMapper.insert(order);
        if(result>0){
            return order;
        }
        return  null;
    }
    /**
     *step4：
     * （2）生成订单编号
     * */
    private  Long generateOrderNO(){
        //时间戳+随机数（100以内）
        return System.currentTimeMillis()+new Random().nextInt(100);
    }
    /*
    * step4：
    * （5）计算订单的总价格的方法
    * */
    private  BigDecimal getOrderPrice(List<OrderItem> orderItemList){

        BigDecimal bigDecimal=new BigDecimal("0");

        for(OrderItem orderItem:orderItemList){
            bigDecimal=BigDecimalUtils.add(bigDecimal.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }

        return bigDecimal;
    }


}
