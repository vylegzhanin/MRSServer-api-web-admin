package io.swagger.client.api;

import com.squareup.okhttp.Call;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Pair;

import java.util.Collections;

import static io.swagger.client.api.MRSS.setupAuthenticationAPIsApi;

/**
 * <p>
 * Created by alexei.vylegzhanin@gmail.com on 5/20/2017.
 */
public class GetSwaggerJson {

    public static void main(String[] args) throws ApiException {
        final ApiClient apiClient = setupAuthenticationAPIsApi();
        final Call call = apiClient.buildCall(
                "/api/" + args[0] + "/" + args[1] + "/swagger.json",
                "GET",
                Collections.<Pair>emptyList(),
                null,
                Collections.<String, String>emptyMap(),
                Collections.<String, Object>emptyMap(),
                new String[0],
                null
                );
        final ApiResponse<String> response = apiClient.execute(call, String.class);

        System.out.println(response.getData());
    }
}
