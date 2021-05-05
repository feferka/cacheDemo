package com.teliacompany.cachedemo.domain;

import lombok.Data;

@Data(staticConstructor = "of")
public class Dto {

    private final String id;
    private final String code;
    private final String data;
}
