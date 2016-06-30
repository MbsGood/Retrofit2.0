package com.yunnex.merge.http;

import com.yunnex.merge.http.data.HttpResult;
import com.yunnex.merge.http.data.TestEntity;
import com.yunnex.merge.http.data.Weather;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * author ChenCHaoXue
 * Created by supercard on 2016/6/23 11:17
 */
public interface RequestApiService {

    @FormUrlEncoded
    @POST("TestEntity")
    Observable<HttpResult<TestEntity>> getData(@Field("appInfo") String appInfo, @Field("platform") String platform, @Field("version") int version);

    @FormUrlEncoded
    @POST("Weather")
    Observable<HttpResult<Weather>> getWeather(@Field("key")String key, @Field("cityname") String cityname);
}
