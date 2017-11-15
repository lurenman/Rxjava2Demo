package com.example.lurenman.rxjava2demo.entity;

/**
 * @author: baiyang.
 * Created on 2017/11/15.
 * 创建一个rxbus实体类
 */

public class RxbusEntity {
    private String message;

    public RxbusEntity(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
