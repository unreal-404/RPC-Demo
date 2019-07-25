package com.unreal.service.impl;

import com.unreal.bean.Person;
import com.unreal.service.PersonService;

public class PersonServiceImpl implements PersonService {

    public Person findOne() {
        Person person = new Person();
        person.setName("unreal");
        person.setAge(21);
        person.setSex("男");
        return person;
    }

    public boolean like(int age) {
        return age > 25 ? true : false;
    }
}
