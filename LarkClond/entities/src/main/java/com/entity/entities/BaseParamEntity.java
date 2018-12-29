package com.entity.entities;

import lombok.Data;

@Data
public class BaseParamEntity<E> {
    private String token;
    private E param;
}
