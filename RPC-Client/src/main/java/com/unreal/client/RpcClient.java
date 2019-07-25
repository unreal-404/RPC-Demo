package com.unreal.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcClient<T>{
    //参数：需要调用的服务接口、实际服务提供地址（IP + 端口）
    public static <T> T getRemoteProxyObj(final Class<?> serviceInterface, final InetSocketAddress address){
        //1.将本地的接口调用转化为JDK的动态代理
        //参数：类加载器（用哪个类加载器去加载代理对象）、动态代理类需要实现的接口、调用处理器（动态代理方法在执行时，会调用里面的invoke方法去执行）
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface},
                new InvocationHandler() {
                    //参数：代理对象（getRemoteProxyObj返回的代理对象）、调用的方法、参数列表
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = null;
                        ObjectOutputStream oos = null;
                        ObjectInputStream ois = null;
                        try {
                            //创建socket客户端，根据指定地址连接远程服务服务提供者
                            socket = new Socket();
                            socket.connect(address);
                            //3.将远程端口调用所需的接口类名、方法名、参数列表等编码后发送给服务消费者
                            oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeUTF(serviceInterface.getName());
                            oos.writeUTF(method.getName());
                            oos.writeObject(method.getParameterTypes());
                            oos.writeObject(args);
                            //4.同步阻塞等待服务器返回应答，取回返回应答
                            ois = new ObjectInputStream(socket.getInputStream());
                            return ois.readObject();
                        }finally {
                                if(null != oos) oos.close();
                                if(null != ois) ois.close();
                                if(null != socket) socket.close();
                        }
                    }
                });
    }
}