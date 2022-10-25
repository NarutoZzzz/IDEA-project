package com.itheima.reggie.controller;

import com.itheima.reggie.service.OrdersDetailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/oderDetail")
public class OrdersDetailController {
    @Resource
    private OrdersDetailService ordersDetailService;

}
