package com.unreal.service;

import com.unreal.bean.Person;

public interface PersonService {

    Person findOne();

    boolean like(int age);
}
