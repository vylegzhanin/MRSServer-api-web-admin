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
 * CreateSnapshotResponse
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-05-20T15:38:57.574Z")
public class CreateSnapshotResponse {
  @SerializedName("snapshotId")
  private String snapshotId = null;

  public CreateSnapshotResponse snapshotId(String snapshotId) {
    this.snapshotId = snapshotId;
    return this;
  }

   /**
   * Id of the created snapshot.
   * @return snapshotId
  **/
  @ApiModelProperty(example = "null", value = "Id of the created snapshot.")
  public String getSnapshotId() {
    return snapshotId;
  }

  public void setSnapshotId(String snapshotId) {
    this.snapshotId = snapshotId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSnapshotResponse createSnapshotResponse = (CreateSnapshotResponse) o;
    return Objects.equals(this.snapshotId, createSnapshotResponse.snapshotId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(snapshotId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateSnapshotResponse {\n");
    
    sb.append("    snapshotId: ").append(toIndentedString(snapshotId)).append("\n");
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

