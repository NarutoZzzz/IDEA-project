package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.OrdersDetail;
import com.itheima.reggie.mapper.OrdersDetailMapper;
import com.itheima.reggie.service.OrdersDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersDetailServiceImpl extends ServiceImpl<OrdersDetailMapper, OrdersDetail> implements OrdersDetailService {

}
