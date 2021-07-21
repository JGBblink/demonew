package com.example.demo_test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JinGuiBo
 * @date 2020-12-08 17:27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BuilderTestEntity {

    private String name;
    private Integer age;
}
