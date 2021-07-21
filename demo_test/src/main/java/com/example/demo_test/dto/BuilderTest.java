package com.example.demo_test.dto;

import cn.hutool.json.JSONUtil;

/**
 * @author JinGuiBo
 * @date 2020-12-08 17:28
 **/
public class BuilderTest {

    public static void main(String[] args) {
        BuilderTestEntity jgb = BuilderTestEntity.builder()
                .name("JGB")
                .age(18)
                .build();

        System.out.println(jgb);
        System.out.println(jgb);
        System.out.println(JSONUtil.toJsonStr(jgb));
    }
}
