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

/**
 * CreateSessionRequest
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-05-20T15:38:57.574Z")
public class CreateSessionRequest {
  @SerializedName("name")
  private String name = null;

  @SerializedName("snapshotId")
  private String snapshotId = null;

  /**
   * Type of the runtime. **Optional [Default R]**
   */
  public enum RuntimeTypeEnum {
    @SerializedName("R")
    R("R"),
    
    @SerializedName("Python")
    PYTHON("Python");

    private String value;

    RuntimeTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("runtimeType")
  private RuntimeTypeEnum runtimeType = null;

  public CreateSessionRequest name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the session. **Optional**
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "Name of the session. **Optional**")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CreateSessionRequest snapshotId(String snapshotId) {
    this.snapshotId = snapshotId;
    return this;
  }

   /**
   * Id of the snapshot to be loaded into the new created session. **Optional**
   * @return snapshotId
  **/
  @ApiModelProperty(example = "null", value = "Id of the snapshot to be loaded into the new created session. **Optional**")
  public String getSnapshotId() {
    return snapshotId;
  }

  public void setSnapshotId(String snapshotId) {
    this.snapshotId = snapshotId;
  }

  public CreateSessionRequest runtimeType(RuntimeTypeEnum runtimeType) {
    this.runtimeType = runtimeType;
    return this;
  }

   /**
   * Type of the runtime. **Optional [Default R]**
   * @return runtimeType
  **/
  @ApiModelProperty(example = "null", value = "Type of the runtime. **Optional [Default R]**")
  public RuntimeTypeEnum getRuntimeType() {
    return runtimeType;
  }

  public void setRuntimeType(RuntimeTypeEnum runtimeType) {
    this.runtimeType = runtimeType;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSessionRequest createSessionRequest = (CreateSessionRequest) o;
    return Objects.equals(this.name, createSessionRequest.name) &&
        Objects.equals(this.snapshotId, createSessionRequest.snapshotId) &&
        Objects.equals(this.runtimeType, createSessionRequest.runtimeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, snapshotId, runtimeType);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateSessionRequest {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    snapshotId: ").append(toIndentedString(snapshotId)).append("\n");
    sb.append("    runtimeType: ").append(toIndentedString(runtimeType)).append("\n");
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

