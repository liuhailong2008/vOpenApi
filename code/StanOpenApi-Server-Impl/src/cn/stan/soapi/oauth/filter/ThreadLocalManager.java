//package com.gwssi.optimus.core.common;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ThreadLocalManager {
//    
//    public final static String RAW_HTTP_REQUEST = "http_request";
//    public final static String RAW_HTTP_RESPONSE = "http_response";
//    public final static String OPTIMUS_REQUEST = "optimus_request";
//    public final static String OPTIMUS_RESPONSE = "optimus_response";
//
//    private static ThreadLocal pool = new ThreadLocal();
//
//    public static Object get(String key) {
//        Map map = (Map) pool.get();
//        if (map == null) {
//            return null;
//        }
//        return map.get(key);
//    }
//
//    public static void add(String key, Object value) {
//        if (pool.get() == null) {
//            pool.set(new HashMap());
//        }
//        Map map = (Map) pool.get();
//        map.put(key, value);
//    }
//
//    public static Map getMap() {
//        return ((Map) pool.get());
//    }
//
//    public static void clear() {
//        pool.set(null);
//    }
//}
