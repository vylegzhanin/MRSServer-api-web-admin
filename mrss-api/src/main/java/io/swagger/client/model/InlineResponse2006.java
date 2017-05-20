/*
 * MRSServer
 * Integration technology for deploying R analytics inside web, desktop, mobile, and dashboard applications as well as backend systems. Turn your R scripts into analytics web services, so code can be easily executed by applications running on a secure server.  The core APIs can be grouped into several categories, including: [Authentication](#authentication-apis), [Web Services](#services-management-apis), [Sessions](#session-apis), [Snapshots](#snapshot-apis), and [Status](#status-apis). 
 *
 * OpenAPI spec version: 9.1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.StatusResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InlineResponse2006
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-05-20T15:38:57.574Z")
public class InlineResponse2006 {
  /**
   * Health of the system.
   */
  public enum StatusCodeEnum {
    @SerializedName("fail")
    FAIL("fail"),
    
    @SerializedName("pass")
    PASS("pass"),
    
    @SerializedName("warn")
    WARN("warn");

    private String value;

    StatusCodeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("statusCode")
  private StatusCodeEnum statusCode = null;

  @SerializedName("details")
  private Map<String, Object> details = new HashMap<String, Object>();

  @SerializedName("components")
  private Map<String, StatusResponse> components = new HashMap<String, StatusResponse>();

  public InlineResponse2006 statusCode(StatusCodeEnum statusCode) {
    this.statusCode = statusCode;
    return this;
  }

   /**
   * Health of the system.
   * @return statusCode
  **/
  @ApiModelProperty(example = "null", value = "Health of the system.")
  public StatusCodeEnum getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(StatusCodeEnum statusCode) {
    this.statusCode = statusCode;
  }

  public InlineResponse2006 details(Map<String, Object> details) {
    this.details = details;
    return this;
  }

  public InlineResponse2006 putDetailsItem(String key, Object detailsItem) {
    this.details.put(key, detailsItem);
    return this;
  }

   /**
   * Details about the health of the system other than the status of its components.
   * @return details
  **/
  @ApiModelProperty(example = "null", value = "Details about the health of the system other than the status of its components.")
  public Map<String, Object> getDetails() {
    return details;
  }

  public void setDetails(Map<String, Object> details) {
    this.details = details;
  }

  public InlineResponse2006 components(Map<String, StatusResponse> components) {
    this.components = components;
    return this;
  }

  public InlineResponse2006 putComponentsItem(String key, StatusResponse componentsItem) {
    this.components.put(key, componentsItem);
    return this;
  }

   /**
   * The status of the components that make up the system.
   * @return components
  **/
  @ApiModelProperty(example = "null", value = "The status of the components that make up the system.")
  public Map<String, StatusResponse> getComponents() {
    return components;
  }

  public void setComponents(Map<String, StatusResponse> components) {
    this.components = components;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse2006 inlineResponse2006 = (InlineResponse2006) o;
    return Objects.equals(this.statusCode, inlineResponse2006.statusCode) &&
        Objects.equals(this.details, inlineResponse2006.details) &&
        Objects.equals(this.components, inlineResponse2006.components);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, details, components);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse2006 {\n");
    
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

