package com.unreal.server.impl;

import com.unreal.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerCenter implements Server {

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final HashMap<String,Class> serviceRegistry = new HashMap<String, Class>();

    private static boolean isRunning = false;

    private static int port;

    public ServerCenter(int port){
        this.port = port;
    }

    public void stop() {
        this.isRunning = false;
        executorService.shutdown();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("============ 服务启动 ============");
        try {
            while (true){
                //监听客户端TCP连接，接收到TCP连接后将其封装为Task，交由线程池进行
                executorService.execute(new ServiceTask(serverSocket.accept()));
                System.out.println("============= 捕捉到请求 =============");
            }
        }finally {
            serverSocket.close();
        }
    }

    public void register(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(),impl);
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public int getPort() {
        return this.port;
    }

    private static class ServiceTask implements Runnable {
        Socket clientSocket = null;
        public ServiceTask(Socket clientSocket){
            this.clientSocket = clientSocket;
        }
        public void run() {
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;
            try {
                ois = new ObjectInputStream(clientSocket.getInputStream());
                String serviceName = ois.readUTF();
                System.out.println("== 请求接口 "+ serviceName + " ==");
                String methodName = ois.readUTF();
                System.out.println("== 请求方法 "+ methodName + " ==");
                Class<?>[] parameterTypes = (Class<?>[]) ois.readObject();
                System.out.println("== 参数类型 "+ parameterTypes.toString() + " ==");
                Object[] arguments = (Object[]) ois.readObject();
                System.out.println("== 参数 "+ arguments + " ==");
                Class serviceClass = serviceRegistry.get(serviceName);
                System.out.println("== 请求接口实例 "+serviceClass.toString() + " ==");
                if (null == serviceClass){
                    System.out.println("============= "+serviceName+" Not Found ! =============");
                }
                Method method = serviceClass.getMethod(methodName,parameterTypes);
                Object result = method.invoke(serviceClass.newInstance(),arguments);
                System.out.println("== 返回结果 "+ result + " ==");
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject(result);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(null != ois){
                    try {
                        ois.close();
                    }catch (IOException ie){
                        ie.printStackTrace();
                    }
                }
                if (null != oos){
                    try {
                        oos.close();
                    }catch (IOException ie){
                        ie.printStackTrace();
                    }
                }
                if(null != clientSocket){
                    try {
                        clientSocket.close();
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }
    }

}
