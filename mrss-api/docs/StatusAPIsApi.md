# StatusAPIsApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**status**](StatusAPIsApi.md#status) | **GET** /status | Get Status


<a name="status"></a>
# **status**
> InlineResponse2006 status()

Get Status

Gets the current health of the system.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.StatusAPIsApi;


StatusAPIsApi apiInstance = new StatusAPIsApi();
try {
    InlineResponse2006 result = apiInstance.status();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatusAPIsApi#status");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**InlineResponse2006**](InlineResponse2006.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

