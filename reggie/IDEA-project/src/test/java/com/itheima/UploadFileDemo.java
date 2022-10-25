package com.itheima;

import org.junit.jupiter.api.Test;

public class UploadFileDemo {
    @Test
    public void test1(){
        String fileName = "erorowe.jpg";
        String substring = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(substring);
    }

}
