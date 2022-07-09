package com.atguigu.gmall.user.observer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ObserverTest {

    public static void main(String[] args) {
        //1、发布者
        AtguiguPublisher publisher = new AtguiguPublisher();



        //2、
        PersonSubscriber lfy = new PersonSubscriber("雷锋杨");
        publisher.guanzhu(lfy);
//
        PersonSubscriber zs = new PersonSubscriber("张三");
        publisher.guanzhu(zs);

        PersonSubscriber ls = new PersonSubscriber("李四");
        publisher.guanzhu(zs);

        //更多观察


        //3、发消息
        publisher.publishVideo("Java入门到精通");


        //消息队列；
        //for(Connection con: Connections){
        //         con.sendMessage("消息")
        // }

        // 1、组合了很多对象。  2、做某个事以后把这些对象都通知了一遍。






    }

}

//1、发布者；   封装、继承、多态
@NoArgsConstructor
@Data
class  AtguiguPublisher{

    //发布者得知道有哪些观察者
    List<PersonSubscriber> fans = new ArrayList<>();

    //来获取观察者
    public void guanzhu(PersonSubscriber fan){
        fans.add(fan);
    }

    //发布消息
    public void publishVideo(String name){
        System.out.println("视频发布："+name);
        //遍历所有观察者通知他们；
        for (PersonSubscriber fan : fans) {
            fan.listenMessage(name);
        }
    }

}


//2、观察者
@AllArgsConstructor
@Data
class PersonSubscriber{

    private String userName;

    public void listenMessage(String name){
        System.out.println("【"+userName+"】收到消息："+name);
    }
}