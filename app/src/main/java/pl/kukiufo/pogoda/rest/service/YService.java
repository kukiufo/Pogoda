package pl.kukiufo.pogoda.rest.service;

import pl.kukiufo.pogoda.model.YResponse;
import retrofit.http.EncodedPath;
import retrofit.http.GET;

/**
 * Created by kukiufo on 26.02.15.
 */
public interface YService {

    @SuppressWarnings("deprecation")
    @GET("/v1/public/yql{service_query}")
    YResponse getResponce(@EncodedPath("service_query") String service_query);
}