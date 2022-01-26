package com.test.mapper.parseHandler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;

/**
 * @program: testWithMaven
 * @description:
 * @author: gwb
 * @create: 2022-01-06 16:44
 **/
public class JsonArrayHandler extends FastjsonTypeHandler {

    private final Class<? extends Object> type;

    public JsonArrayHandler(Class<?> type) {
        super(type);
        this.type = type;
    }

    @Override
    protected Object parse(String json) {
        return JSON.parseArray(json);
    }

    @Override
    protected String toJson(Object obj) {
        return super.toJson(obj);
    }
}
