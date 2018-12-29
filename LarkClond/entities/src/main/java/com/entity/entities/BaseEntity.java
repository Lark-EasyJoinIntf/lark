package com.entity.entities;

import lombok.Data;

@Data
public class BaseEntity<E> {
    private String token;
    private E data;
}
