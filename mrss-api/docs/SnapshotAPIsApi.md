# SnapshotAPIsApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createSnapshot**](SnapshotAPIsApi.md#createSnapshot) | **POST** /sessions/{id}/snapshot | Create Snapshot
[**deleteSnapshot**](SnapshotAPIsApi.md#deleteSnapshot) | **DELETE** /snapshots/{id} | Delete Snapshot
[**getSnapshotContent**](SnapshotAPIsApi.md#getSnapshotContent) | **GET** /snapshots/{id} | Get Snapshot
[**listSnapshots**](SnapshotAPIsApi.md#listSnapshots) | **GET** /snapshots | Get Snapshots
[**loadSnapshot**](SnapshotAPIsApi.md#loadSnapshot) | **POST** /sessions/{id}/loadsnapshot/{snapshotId} | Load Snapshot


<a name="createSnapshot"></a>
# **createSnapshot**
> InlineResponse2011 createSnapshot(id, createSnapshotRequest)

Create Snapshot

Create a snapshot from session by saving the workspace and all files in the working directory into zip file, the return value will be the created snapshot Id.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.SnapshotAPIsApi;


SnapshotAPIsApi apiInstance = new SnapshotAPIsApi();
String id = "id_example"; // String | Id of the session.
CreateSnapshotRequest createSnapshotRequest = new CreateSnapshotRequest(); // CreateSnapshotRequest | Properties of the new snapshot.
try {
    InlineResponse2011 result = apiInstance.createSnapshot(id, createSnapshotRequest);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SnapshotAPIsApi#createSnapshot");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Id of the session. |
 **createSnapshotRequest** | [**CreateSnapshotRequest**](CreateSnapshotRequest.md)| Properties of the new snapshot. |

### Return type

[**InlineResponse2011**](InlineResponse2011.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteSnapshot"></a>
# **deleteSnapshot**
> String deleteSnapshot(id)

Delete Snapshot

Delete a snapshot permanently and also it&#39;s content (zip file containing the working directory files and the workspace file)

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.SnapshotAPIsApi;


SnapshotAPIsApi apiInstance = new SnapshotAPIsApi();
String id = "id_example"; // String | Id of the snapshot.
try {
    String result = apiInstance.deleteSnapshot(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SnapshotAPIsApi#deleteSnapshot");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Id of the snapshot. |

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getSnapshotContent"></a>
# **getSnapshotContent**
> File getSnapshotContent(id)

Get Snapshot

Get the snapshot content as zip file stream (zip file containing the working directory files and the workspace file)

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.SnapshotAPIsApi;


SnapshotAPIsApi apiInstance = new SnapshotAPIsApi();
String id = "id_example"; // String | Id of the snapshot.
try {
    File result = apiInstance.getSnapshotContent(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SnapshotAPIsApi#getSnapshotContent");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Id of the snapshot. |

### Return type

[**File**](File.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="listSnapshots"></a>
# **listSnapshots**
> List&lt;InlineResponse2005&gt; listSnapshots()

Get Snapshots

List all the snapshots for the current user and display some info such as Id, display name, creation time, zip file size and owner for this snapshot.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.SnapshotAPIsApi;


SnapshotAPIsApi apiInstance = new SnapshotAPIsApi();
try {
    List<InlineResponse2005> result = apiInstance.listSnapshots();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SnapshotAPIsApi#listSnapshots");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;InlineResponse2005&gt;**](InlineResponse2005.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="loadSnapshot"></a>
# **loadSnapshot**
> loadSnapshot(id, snapshotId)

Load Snapshot

Loads a snapshot into session by merging the workspace of the saved snapshot and target session. It will override the files in the working directory as well.   **Note - ** You can only load R snapshots to R session and Python snapshots to a Python session.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.SnapshotAPIsApi;


SnapshotAPIsApi apiInstance = new SnapshotAPIsApi();
String id = "id_example"; // String | Id of the session.
String snapshotId = "snapshotId_example"; // String | Id of the saved snapshot.
try {
    apiInstance.loadSnapshot(id, snapshotId);
} catch (ApiException e) {
    System.err.println("Exception when calling SnapshotAPIsApi#loadSnapshot");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **String**| Id of the session. |
 **snapshotId** | **String**| Id of the saved snapshot. |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

