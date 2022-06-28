package com.atguigu.gmall.product;


import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class ReflectTest {

    @Test
    void test01(){
        Person person = new Person();

        for (Method method : person.getClass().getMethods()) {
            if(method.getName().contains("getPerson")){
                System.out.println(method.getName()+"：返回值类型"+method.getGenericReturnType());
            }

        }


    }
}


