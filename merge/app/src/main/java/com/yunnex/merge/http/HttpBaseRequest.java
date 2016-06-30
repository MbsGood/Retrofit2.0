package com.yunnex.merge.http;

import android.util.Log;

import com.yunnex.merge.http.data.HttpResult;
import com.yunnex.merge.http.data.Weather;
import com.yunnex.merge.utils.JsonUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author ChenCHaoXue
 * Created by supercard on 2016/6/22 18:02
 */
public class HttpBaseRequest {
    public static final String TAG = "HttpBaseRequest";
    /**
     * 请求的总路径
     */
    public static final String BASE_URL = "http://apis.haoservice.com/";
    private static final int DEFAULT_TIMEOUT = 5;
    RequestApiService requestService;

    public HttpBaseRequest() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.sslSocketFactory()//证书
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.interceptors().add(new Interceptor() {//拦截器 ，可获取请求内容与响应回来的浏览器信息
            @Override
            public Response intercept(Chain chain) throws IOException {
                long t1 = System.nanoTime();
                Request request = chain.request();
                Log.e(TAG, "request.body:" + JsonUtils.toJson(request.body()));//请求参数
                Logger.getLogger(String.format(Locale.CHINA,"Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
                Response response = chain.proceed(request);//请求响应体
                long t2 = System.nanoTime();
                Logger.getLogger(String.format(Locale.CHINA,"Received response for %s in %.1fms%n%s",request.url(), (t2 - t1) / 1e6d, response.headers()));
                return response;
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())// 使用RxJava作为回调适配器
                .baseUrl(BASE_URL)
                .build();
        requestService = retrofit.create(RequestApiService.class);
    }

    /**
     * 内部类的单例实现
     */
    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpBaseRequest INSTANCE = new HttpBaseRequest();
    }

    //获取单例
    public static HttpBaseRequest getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
        @Override
        public T call(HttpResult<T> httpResult) {
            /**
             * 异常情况
             * */
            if (httpResult.getResultCode() != 200) {
                throw new ApiException(100);
            }
            return httpResult.getData();
        }
    }

    public void getWeatherData(Subscriber<HttpResult<Weather>> subscriber) {
        //  返回实体 result = service.请求的接口名称(请求参数);
        Observable observable = requestService.getWeather("32cac184400745d29caafa68b02dcc48", "广州")
                .map(new HttpResultFunc<Weather>());
        toSubscribe(observable, subscriber);
    }
}
