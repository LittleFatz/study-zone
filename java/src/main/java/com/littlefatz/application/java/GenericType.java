package com.littlefatz.application.java;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GenericType {

    public static <T> Type getGenericRuntimeType(Wrapper<T> wrapper) {
        Type type = wrapper.getClass().getGenericSuperclass();
        if (type == null) {
            return null;
        }

        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType)type).getActualTypeArguments();
            return types[0];
        }
        return null;
    }

    public static void main(String[] args) {

        Type type1 = GenericType.getGenericRuntimeType(new Wrapper<List<String>>());
        Type type2 = GenericType.getGenericRuntimeType(new Wrapper<List<String>>() {});

        Type type3 = GenericType.getGenericRuntimeType(new ExtendWrapper() {});

        System.out.println(type1);
        System.out.println(type2);

        System.out.println(type3);
    }

}
