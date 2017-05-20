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
 * ParameterDefinition
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-05-20T15:38:57.574Z")
public class ParameterDefinition {
  @SerializedName("name")
  private String name = null;

  /**
   * The type of the parameter object.
   */
  public enum TypeEnum {
    @SerializedName("logical")
    LOGICAL("logical"),
    
    @SerializedName("numeric")
    NUMERIC("numeric"),
    
    @SerializedName("integer")
    INTEGER("integer"),
    
    @SerializedName("character")
    CHARACTER("character"),
    
    @SerializedName("vector")
    VECTOR("vector"),
    
    @SerializedName("matrix")
    MATRIX("matrix"),
    
    @SerializedName("data.frame")
    DATA_FRAME("data.frame");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("type")
  private TypeEnum type = null;

  public ParameterDefinition name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The name of the parameter object.
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "The name of the parameter object.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ParameterDefinition type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * The type of the parameter object.
   * @return type
  **/
  @ApiModelProperty(example = "null", value = "The type of the parameter object.")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParameterDefinition parameterDefinition = (ParameterDefinition) o;
    return Objects.equals(this.name, parameterDefinition.name) &&
        Objects.equals(this.type, parameterDefinition.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParameterDefinition {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

