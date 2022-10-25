package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

// TODO: 2022/10/20
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;


    /*
    * 根据id删除分类，删除之前需要进行判断
    * */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        queryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(queryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务一场
        if (count1>0){
            //已经关联菜品，抛出一个业务异常
            throw  new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务一场
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count();
        if (count2>0){
            //已经关联套餐，掏出一个业务异常
            throw  new CustomException("关联了套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);
        }
    }
