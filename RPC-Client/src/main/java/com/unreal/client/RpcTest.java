package com.unreal.client;

import com.unreal.service.PersonService;

import java.net.InetSocketAddress;

public class RpcTest {
    public static void main(String[] args) {
        //设置代理对象
        PersonService service = RpcClient.getRemoteProxyObj(PersonService.class,new InetSocketAddress("127.0.0.1",1259));
        System.out.println(service.findOne());
        System.out.println(service.like(22));
    }
}
