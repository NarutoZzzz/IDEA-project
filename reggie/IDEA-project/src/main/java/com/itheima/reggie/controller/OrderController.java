package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/order")
@Transactional
public class OrderController {
    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private OrdersService ordersService;

    @Resource
    private UserService userService;

    @Resource
    private AddressBookService addressBookService;

    @Resource
    private OrdersDetailService ordersDetailService;
    /*
    * 用户下单
    * */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        //获得当前用户id
        long userId = BaseContext.getCurrentId();
        //查询当前用户的购物车数据
//        long orderid = IdWorker.getId();//订单号
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != 0,ShoppingCart::getUserId,userId);
//        queryWrapper.eq(userId != null,ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCart = shoppingCartService.list(queryWrapper);
        if (shoppingCart == null || shoppingCart.size() == 0){
            throw new CustomException("购物车为空，不能下单");
        }

        //查询用户数据
        User user = userService.getById(userId);
        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null ){
            throw new CustomException("用户地址信息有误或为空，不能下单");
        }

        long orderid = IdWorker.getId();//订单号

        AtomicInteger amount = new AtomicInteger(0);

        List<OrdersDetail> orderDetails = shoppingCart.stream().map((item) -> {
            OrdersDetail ordersDetail = new OrdersDetail();
            ordersDetail.setOrderId(orderid);
            ordersDetail.setNumber(item.getNumber());
            ordersDetail.setDishFlavor(item.getDishFlavor());
            ordersDetail.setSetmealId(item.getSetmealId());
            ordersDetail.setName(item.getName());
            ordersDetail.setImage(item.getImage());
            ordersDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return ordersDetail;
        }).collect(Collectors.toList());

        orders.setId(orderid);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderid));
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "":addressBook.getProvinceName())
                        + (addressBook.getCityName() == null ? "":addressBook.getCityName())
                        +(addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                        +(addressBook.getDetail() == null ? "" :addressBook.getDetail()));


        //向订单表插入数据，一条数据
        ordersService.save(orders);
        //向订单明细表插入数据
        ordersDetailService.saveBatch(orderDetails);
        //清空购物车数据
        shoppingCartService.remove(queryWrapper);
        return R.success("下单成功");
        //
    }


}
