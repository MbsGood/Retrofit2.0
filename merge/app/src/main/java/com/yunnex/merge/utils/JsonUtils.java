package com.yunnex.merge.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;


/**
 * 包含操作 {@code JSON} 数据的常用方法的工具类。
 * <p />
 * 该工具类使用的 {@code JSON} 转换引擎是 <a href="http://code.google.com/p/google-gson/"
 * mce_href="http://code.google.com/p/google-gson/" target="_blank">
 * {@code Google Gson}</a>。
 * 
 * 下面是工具类的使用案例：
 * 
 * <pre>
 * public class User {
 *     @SerializedName("pwd")
 *     private String password;
 *     @Expose
 *     @SerializedName("uname")
 *     private String username;
 *     @Expose
 *     @Since(1.1)
 *     private String gender;
 *     @Expose
 *     @Since(1.0)
 *     private String sex;
 * 
 *     public User() {}
 *     public User(String username, String password, String gender) {
 *         // user constructor code... ... ...
 *     }
 * 
 *     public String getUsername()
 *     ... ... ...
 * }
 * List<User> userList = new LinkedList<User>();
 * User jack = new User("Jack", "123456", "Male");
 * User marry = new User("Marry", "888888", "Female");
 * userList.add(jack);
 * userList.add(marry);
 * Type targetType = new TypeToken<List<User>>(){}.getType();
 * String sUserList1 = JSONUtils.toJson(userList, targetType);
 * sUserList1 ----> [{"uname":"jack","gender":"Male","sex":"Male"},{"uname":"marry","gender":"Female","sex":"Female"}]
 * String sUserList2 = JSONUtils.toJson(userList, targetType, false);
 * sUserList2 ----> [{"uname":"jack","pwd":"123456","gender":"Male","sex":"Male"},{"uname":"marry","pwd":"888888","gender":"Female","sex":"Female"}]
 * String sUserList3 = JSONUtils.toJson(userList, targetType, 1.0d, true);
 * sUserList3 ----> [{"uname":"jack","sex":"Male"},{"uname":"marry","sex":"Female"}]
 * </pre>
 * 
 * @since ay-commons-lang 1.0
 * @version 1.1.0
 */
public class JsonUtils {
    /** 空的 {@code JSON} 数据 - <code>"{}"</code>。 */
    public static final String EMPTY_JSON           = "{}";
    public static final String NULL_JSON            = "null";
    /** 空的 {@code JSON} 数组(集合)数据 - {@code "[]"}。 */
    public static final String EMPTY_JSON_ARRAY     = "[]";
    public static final String NULL_JSON_ARRAY      = "[null]";
    /** 默认的 {@code JSON} 日期/时间字段的格式化模式。 */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /** {@code Google Gson} 的 <code>@Since</code> 注解常用的版本号常量 - {@code 1.0}。 */
    public static final double SINCE_VERSION_10     = 1.0d;
    /** {@code Google Gson} 的 <code>@Since</code> 注解常用的版本号常量 - {@code 1.1}。 */
    public static final double SINCE_VERSION_11     = 1.1d;
    /** {@code Google Gson} 的 <code>@Since</code> 注解常用的版本号常量 - {@code 1.2}。 */
    public static final double SINCE_VERSION_12     = 1.2d;
    /** {@code Google Gson} 的 <code>@Until</code> 注解常用的版本号常量 - {@code 1.0}。 */
    public static final double UNTIL_VERSION_10     = SINCE_VERSION_10;
    /** {@code Google Gson} 的 <code>@Until</code> 注解常用的版本号常量 - {@code 1.1}。 */
    public static final double UNTIL_VERSION_11     = SINCE_VERSION_11;
    /** {@code Google Gson} 的 <code>@Until</code> 注解常用的版本号常量 - {@code 1.2}。 */
    public static final double UNTIL_VERSION_12     = SINCE_VERSION_12;

    /**
     * <p>
     * <code>JSONUtils</code> instances should NOT be constructed in standard
     * programming. Instead, the class should be used as
     * <code>JSONUtils.fromJson("foo");</code>.
     * </p>
     * <p>
     * This constructor is public to permit tools that require a JavaBean
     * instance to operate.
     * </p>
     */
    public JsonUtils() {
        super();
    }

    /***
     * 判断字符串是否为有内容
     * 
     * @param string
     * @return
     */
    private static boolean isBlankString(String string) {
        if( string == null || "".equals(string) ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将给定的目标对象根据指定的条件参数转换成 {@code JSON} 格式的字符串。
     * <p />
     * <strong>该方法转换发生错误时，不会抛出任何异常。若发生错误时，普通对象返回 <code>"{}"</code>； 集合或数组对象返回
     * <code>"[]"</code> </strong>
     * 
     * @param target 目标对象。
     * @param targetType 目标对象的类型。
     * @param isSerializeNulls 是否序列化 {@code null} 值字段。
     * @param version 字段的版本号注解。
     * @param datePattern 日期字段的格式化模式。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean isSerializeNulls, Double version, String datePattern, boolean excludesFieldsWithoutExpose) {
        if( target == null )
            return EMPTY_JSON;
        GsonBuilder builder = new GsonBuilder();
        if( isSerializeNulls ) {
            builder.serializeNulls();
        }
        if( version != null ) {
            builder.setVersion(version.doubleValue());
        }
        if( isBlankString(datePattern) ) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
//        builder.setDateFormat(datePattern)
//                .registerTypeAdapter(OrderInfoBean.class, orderInfoBeanAdapter)
//                .registerTypeAdapter(OffLinePayOrdersInfoBean.class, orderInfoBeanAdapter)
//                .registerTypeAdapter(AliPayOrdersInfoBean.class, orderInfoBeanAdapter);
        // if (excludesFieldsWithoutExpose){
        // builder.excludeFieldsWithoutExposeAnnotation();
        // }
        return toJson(target, targetType, builder);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target) {
        return toJson(target, null, false, null, null, false);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param datePattern 日期字段的格式化模式。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, String datePattern) {
        return toJson(target, null, false, null, datePattern, true);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param version 字段的版本号注解({@literal @Since})。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Double version) {
        return toJson(target, null, false, version, null, true);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, null, null, excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法只用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param version 字段的版本号注解({@literal @Since})。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, null, false, version, null, excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType) {
        return toJson(target, targetType, false, null, null, true);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法只会转换标有 {@literal @Expose} 注解的字段；</li>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param version 字段的版本号注解({@literal @Since})。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, Double version) {
        return toJson(target, targetType, false, version, null, true);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法会转换所有未标注或已标注 {@literal @Since} 的字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, null, null, excludesFieldsWithoutExpose);
    }

    /**
     * 将给定的目标对象转换成 {@code JSON} 格式的字符串。<strong>此方法通常用来转换使用泛型的对象。</strong>
     * <ul>
     * <li>该方法不会转换 {@code null} 值字段；</li>
     * <li>该方法转换时使用默认的 日期/时间 格式化模式 - {@code yyyy-MM-dd HH:mm:ss}；</li>
     * </ul>
     * 
     * @param target 要转换成 {@code JSON} 的目标对象。
     * @param targetType 目标对象的类型。
     * @param version 字段的版本号注解({@literal @Since})。
     * @param excludesFieldsWithoutExpose 是否排除未标注 {@literal @Expose} 注解的字段。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.0
     */
    public static String toJson(Object target, Type targetType, Double version, boolean excludesFieldsWithoutExpose) {
        return toJson(target, targetType, false, version, null, excludesFieldsWithoutExpose);
    }
    
    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     * 
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param  {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
     * @param datePattern 日期格式模式。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Type type) throws Exception{
        if( isBlankString(json) ) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
//        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        
        String datePattern = DEFAULT_DATE_PATTERN;
        builder.setDateFormat(datePattern);
        Gson gson = builder.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(boolean.class, booleanAsIntAdapter)

                .excludeFieldsWithModifiers(Modifier.FINAL).create();
        return gson.fromJson(json, type);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     * 
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param token {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
     * @param datePattern 日期格式模式。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
        if( isBlankString(json) ) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
//        builder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        
        if( isBlankString(datePattern) ) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        Gson gson = builder.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
                .registerTypeAdapter(boolean.class, booleanAsIntAdapter)

                .excludeFieldsWithModifiers(Modifier.FINAL).create();

        try {
            return gson.fromJson(json, token.getType());

        } catch (Exception ex) {
           ex.printStackTrace();
          
            return null;
        }
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。
     * 
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param token {@code com.google.gson.reflect.TypeToken} 的类型指示类对象。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        return fromJson(json, token, null);
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     * 
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param clazz 要转换的目标类。
     * @param datePattern 日期格式模式。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
        if( isBlankString(json) ) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder();
        if( isBlankString(datePattern) ) {
            datePattern = DEFAULT_DATE_PATTERN;
        }
        builder.setDateFormat(datePattern);
        Gson gson = builder.create();
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception ex) {
            // Common.log("ws",json + " 无法转换为 " + clazz.getName() +
            // " 对象!异常原因："+ex.getMessage());
            return null;
        }
    }

    /**
     * 将给定的 {@code JSON} 字符串转换成指定的类型对象。<strong>此方法通常用来转换普通的 {@code JavaBean}
     * 对象。</strong>
     * 
     * @param <T> 要转换的目标类型。
     * @param json 给定的 {@code JSON} 字符串。
     * @param clazz 要转换的目标类。
     * @return 给定的 {@code JSON} 字符串表示的指定的类型对象。
     * @since 1.0
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, null);
    }

    /**
     * 将给定的目标对象根据{@code GsonBuilder} 所指定的条件参数转换成 {@code JSON} 格式的字符串。
     * <p />
     * 该方法转换发生错误时，不会抛出任何异常。若发生错误时，{@code JavaBean} 对象返回 <code>"{}"</code>；
     * 集合或数组对象返回 <code>"[]"</code>。 其本基本类型，返回相应的基本值。
     * 
     * @param target 目标对象。
     * @param targetType 目标对象的类型。
     * @param builder 可定制的{@code Gson} 构建器。
     * @return 目标对象的 {@code JSON} 格式的字符串。
     * @since 1.1
     */
    public static String toJson(Object target, Type targetType, GsonBuilder builder) {
        if( target == null )
            return EMPTY_JSON;
        Gson gson = null;
        if( builder == null ) {
            gson = new Gson();
        } else {
            gson = builder.create();
        }
        String result = EMPTY_JSON;
        try {
            if( targetType == null ) {
                result = gson.toJson(target);
            } else {
                result = gson.toJson(target, targetType);
            }
        } catch (Exception ex) {
            // Common.log("ws","目标对象 " + target.getClass().getName() +
            // " 转换 JSON 字符串时，发生异常！异常原因："+ex.getMessage());
            if( target instanceof Collection<?> || target instanceof Iterator<?>
                    || target instanceof Enumeration<?> || target.getClass().isArray() ) {
                result = EMPTY_JSON_ARRAY;
            }
        }
        return result;
    }

    private static final TypeAdapter<Boolean> booleanAsIntAdapter  = new TypeAdapter<Boolean>()
                                                                   {
                                                                       @Override
                                                                       public void write(JsonWriter out, Boolean value)
                                                                               throws IOException {
                                                                           if( value == null ) {
                                                                               out.nullValue();
                                                                           } else {
                                                                               out.value(value);
                                                                           }
                                                                       }

                                                                       @Override
                                                                       public Boolean read(JsonReader in)
                                                                               throws IOException {
                                                                           JsonToken peek = in
                                                                                   .peek();
                                                                           switch (peek) {
                                                                               case BOOLEAN:
                                                                                   return in
                                                                                           .nextBoolean();
                                                                               case NULL:
                                                                                   in.nextNull();
                                                                                   return null;
                                                                               case NUMBER:
                                                                                   return in
                                                                                           .nextInt() != 0;
                                                                               case STRING:
                                                                                   return Boolean
                                                                                           .parseBoolean(in
                                                                                                   .nextString());
                                                                               default:
                                                                                   throw new IllegalStateException(
                                                                                           "Expected BOOLEAN or NUMBER but was "
                                                                                                   + peek);
                                                                           }
                                                                       }
                                                                   };

}