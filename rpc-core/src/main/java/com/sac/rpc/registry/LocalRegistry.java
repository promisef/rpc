package com.sac.rpc.registry;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LocalRegistry {

    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    public static void register(String name, Class<?> Implclass) {
        map.put(name, Implclass);
    }

    public static Class<?> get(String name) {
        return map.get(name);
    }

    public static void remove(String name) {
        map.remove(name);
    }
}
