package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IOrderService {

  /*
  * 创建订单
  * */
  ServerResponse createOrder(Integer userId,Integer shippingId);
}

