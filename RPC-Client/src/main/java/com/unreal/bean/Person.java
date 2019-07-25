package com.unreal.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
/**
 * 样例实体
 */
public class Person implements Serializable {
    private String name;
    private String sex;
    private int age;
}
