package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /*
    * 自定义的元数据对象处理器
    * */

    @Override
    public void insertFill(MetaObject metaObject) {
            /*
            * 插入操作自动填充
            * */
            log.info("公共字段自动填充[INSERT]");
            metaObject.setValue("createTime", LocalDateTime.now());
            metaObject.setValue("updateTime", LocalDateTime.now());
            metaObject.setValue("createUser", BaseContext.getCurrentId());
            metaObject.setValue("updateUser", BaseContext.getCurrentId());

            log.info(metaObject.toString());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        /*
        * 更新操作自动填充
        * */
            log.info("公共字段自动填充[UPDATE]");
            log.info(metaObject.toString());
            Long id = Thread.currentThread().getId();
            log.info("当前线程为：{}",id);

            metaObject.setValue("updateTime",LocalDateTime.now());
            metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
