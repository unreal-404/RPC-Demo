package com.unreal.enter;

import com.unreal.server.Server;
import com.unreal.server.impl.ServerCenter;
import com.unreal.service.PersonService;
import com.unreal.service.impl.PersonServiceImpl;

public class ServerStart {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                try{
                    Server server = new ServerCenter(1259);
                    server.register(PersonService.class, PersonServiceImpl.class);
                    server.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    RPC（Remote Procedure Call）—远程过程调用，它是一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。
//    RPC协议假定某些传输协议的存在，如TCP或UDP，为通信程序之间携带信息数据。
//    在OSI网络通信模型中，RPC跨越了传输层和应用层。RPC使得开发包括网络分布式多程序在内的应用程序更加容易。
//
//    用生活中的实例来打比方：
//    假如，我女朋友需要喝水，让我去接水，但家里饮水机没水了，需要另外搬一桶水，搬一桶水很累，这个时候怎么办呢？
//    这时候发现隔壁有水，是不是向邻居打个招呼就可以了，然后拿着杯子去接水就可以了，然后交给女朋友。
//    这个过程中，女朋友只是需要喝水，她只要结果就行了，她并不需要知道你是怎么取到水的，不知道的话，看起来，就像是在自己家里接的。
//
//    假如，我女朋友（服务消费者）需要喝水，让我（代理）去接水（执行的方法），但家里饮水机（Service）没水了，需要另外搬一桶水（写ServiceImpl），搬一桶水很累，这个时候怎么办呢？
//    这时候发现隔壁有水（其他服务器提供的方法），是不是向邻居打个招呼就可以了（远程调用），然后拿着杯子去接水就可以了，然后交给女朋友（返回结果）。
//    这个过程中，女朋友只是需要喝水，她只要结果就行了，她并不需要知道你是怎么取到水的，不知道的话，看起来，就像是在自己家里接的。
}
