package com.ylzb.fastpay.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * json解析
 */
public class JsonUtil {

    //解决json转map，int变成double类型的问题
    public static Gson getGson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(HashMap.class, new JsonDeserializer<HashMap>() {
            @Override
            public HashMap<String, Object> deserialize(JsonElement json, Type typeOfT,
                                                       JsonDeserializationContext context) throws JsonParseException {
                HashMap<String, Object> resultMap = new HashMap<>();
                JsonObject jsonObject = json.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet) {
                    resultMap.put(entry.getKey(), entry.getValue());
                }
                return resultMap;
            }

        }).create();
        return gson;
    }

    //json变成类
    public static Object parsData(String json, Class clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    //json变成类(含泛型)
    public static Object parsData(String json, TypeToken type) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, type.getType());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


    //json变成类(含泛型)
    public static Object parsData(String json, Type type) {
        try {
            Gson gson = new Gson();
            Object result = gson.fromJson(json, type);
            return result;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    //json对象转字符串
    public static String objectToString(Object o) {
        try {
            Gson gson = new GsonBuilder()
//                    .serializeNulls()
                    .create();
            return gson.toJson(o);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
    //json对象转字符串 过滤0
//    public static String objectToStringFilter0(Object o) {
//        try {
//
//
//            GsonBuilder gsonBuilder = new GsonBuilder();
//            gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
//                @Override
//                public boolean shouldSkipField(FieldAttributes f) {
//                    return f.getName().contains("_");
//                }
//
//                @Override
//                public boolean shouldSkipClass(Class<?> clazz) {
//                    return false;
//                }
//            });
//            Gson gson = gsonBuilder.serializeNulls().create();
////            Gson gson = new GsonBuilder()
////                    .serializeNulls()
////                    .create();
//            return gson.toJson(o);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        return null;
//    }

    //json变成类（有集合有泛型） 可能出现列表中的数据解析出来是LinkTreeMap，导致解析失败
    public static <T> Object parsListData(String json, Class clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


    /**
     * json转list 解决解析出来的数据解析出来是LinkTreeMap问题
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseToList(String json, Class clazz) {
        try {
            Type type = new ParameterizedTypeImpl(clazz);
            List<T> list = new Gson().fromJson(json, type);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * HashMap<String,Integer>为例
     */
    private  static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        /**
         * 返回实际类型组成的数据，即new Type[]{String.class,Integer.class}
         * @return
         */
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        /**
         *  返回原生类型，即 HashMap
         * @return
         */
        @Override
        public Type getRawType() {
            return List.class;
        }

        /**
         *  返回 Type 对象，表示此类型是其成员之一的类型。
         *  例如，如果此类型为 O<T>.I<S>，则返回 O<T> 的表示形式。 如果此类型为顶层类型，则返回 null。这里就直接返回null就行了。
         * @return
         */
        @Override
        public Type getOwnerType() {
            return null;
        }
    }

}
