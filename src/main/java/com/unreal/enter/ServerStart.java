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
}
