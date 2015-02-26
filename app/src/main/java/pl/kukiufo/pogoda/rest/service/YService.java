package pl.kukiufo.pogoda.rest.service;

import pl.kukiufo.pogoda.model.YResponse;
import retrofit.http.EncodedPath;
import retrofit.http.GET;

/**
 * Created by kukiufo on 26.02.15.
 */
public interface YService {

    @GET("/v1/public/yql{service_query}")
    //YUnits units(@Path("url_city_name") String url_city_name);

//    @GET("/v1/public/yql")
    YResponse getResponce(@EncodedPath("service_query") String service_query);
}