package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService  {

    @Resource
    private DishFlavorService dishFlavorService;


    //新增菜品，同时保存对应的口味数据
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
          item.setDishId(dishId);
          return item;
       }).collect(Collectors.toList());
        //保存口味基本信息到口味表
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dishId = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dishId,dishDto);

        //查询当前菜品对应的口味信息，从dish——falvor表中查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishId.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    //更新口味信息
    @Transactional //事务注解
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //更新菜品口味表,清理当前菜品对应口味数据 dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        dishFlavorService.remove(queryWrapper.eq(DishFlavor::getDishId,dishDto.getId()));
        //添加当前提交过来的口味数据 dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        //遍历当前falvors
        flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
