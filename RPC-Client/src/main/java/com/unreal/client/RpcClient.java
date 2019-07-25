package com.unreal.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcClient<T>{

    public static <T> T getRemoteProxyObj(final Class<?> serviceInterface, final InetSocketAddress address){
        //1.将本地的接口调用转化为JDK的动态代理
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface},
                new InvocationHandler() {
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