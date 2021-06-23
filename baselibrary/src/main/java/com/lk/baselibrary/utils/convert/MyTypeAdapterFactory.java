package com.lk.baselibrary.utils.convert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * @author: Administrator
 * @date: 2021/6/21
 */
public class MyTypeAdapterFactory<T> implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (rawType == Float.class || rawType == float.class) {
            return (TypeAdapter<T>) new FloatNullAdapter();
        } else if (rawType == Integer.class || rawType == int.class) {
            return (TypeAdapter<T>) new IntNullAdapter();
        } else if (rawType == Double.class || rawType == double.class) {
            return (TypeAdapter<T>) new DoubleNullAdapter();
        }
        return null;
    }
}
