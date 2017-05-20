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
import io.swagger.client.model.ServicesInputParameterDefinitions;
import java.util.ArrayList;
import java.util.List;

/**
 * PublishRequest1
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-05-20T15:38:57.574Z")
public class PublishRequest1 {
  @SerializedName("snapshotId")
  private String snapshotId = null;

  @SerializedName("code")
  private String code = null;

  @SerializedName("description")
  private String description = null;

  @SerializedName("operationId")
  private String operationId = null;

  @SerializedName("inputParameterDefinitions")
  private List<ServicesInputParameterDefinitions> inputParameterDefinitions = new ArrayList<ServicesInputParameterDefinitions>();

  @SerializedName("outputParameterDefinitions")
  private List<ServicesInputParameterDefinitions> outputParameterDefinitions = new ArrayList<ServicesInputParameterDefinitions>();

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

  @SerializedName("initCode")
  private String initCode = null;

  @SerializedName("outputFileNames")
  private List<String> outputFileNames = new ArrayList<String>();

  public PublishRequest1 snapshotId(String snapshotId) {
    this.snapshotId = snapshotId;
    return this;
  }

   /**
   * ID of the snapshot to be used for service. **Optional**
   * @return snapshotId
  **/
  @ApiModelProperty(example = "null", value = "ID of the snapshot to be used for service. **Optional**")
  public String getSnapshotId() {
    return snapshotId;
  }

  public void setSnapshotId(String snapshotId) {
    this.snapshotId = snapshotId;
  }

  public PublishRequest1 code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Code to execute. Specific to the runtime type. **<font color = 'red'>Required</font>**
   * @return code
  **/
  @ApiModelProperty(example = "null", value = "Code to execute. Specific to the runtime type. **<font color = 'red'>Required</font>**")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public PublishRequest1 description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Description for the web service. **Optional**
   * @return description
  **/
  @ApiModelProperty(example = "null", value = "Description for the web service. **Optional**")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public PublishRequest1 operationId(String operationId) {
    this.operationId = operationId;
    return this;
  }

   /**
   * Swagger operationId/alias for web service. **Optional**
   * @return operationId
  **/
  @ApiModelProperty(example = "null", value = "Swagger operationId/alias for web service. **Optional**")
  public String getOperationId() {
    return operationId;
  }

  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public PublishRequest1 inputParameterDefinitions(List<ServicesInputParameterDefinitions> inputParameterDefinitions) {
    this.inputParameterDefinitions = inputParameterDefinitions;
    return this;
  }

  public PublishRequest1 addInputParameterDefinitionsItem(ServicesInputParameterDefinitions inputParameterDefinitionsItem) {
    this.inputParameterDefinitions.add(inputParameterDefinitionsItem);
    return this;
  }

   /**
   * Input parameters definitions for the execution. **Optional**
   * @return inputParameterDefinitions
  **/
  @ApiModelProperty(example = "null", value = "Input parameters definitions for the execution. **Optional**")
  public List<ServicesInputParameterDefinitions> getInputParameterDefinitions() {
    return inputParameterDefinitions;
  }

  public void setInputParameterDefinitions(List<ServicesInputParameterDefinitions> inputParameterDefinitions) {
    this.inputParameterDefinitions = inputParameterDefinitions;
  }

  public PublishRequest1 outputParameterDefinitions(List<ServicesInputParameterDefinitions> outputParameterDefinitions) {
    this.outputParameterDefinitions = outputParameterDefinitions;
    return this;
  }

  public PublishRequest1 addOutputParameterDefinitionsItem(ServicesInputParameterDefinitions outputParameterDefinitionsItem) {
    this.outputParameterDefinitions.add(outputParameterDefinitionsItem);
    return this;
  }

   /**
   * Output parameter definitions for the execution. **Optional**
   * @return outputParameterDefinitions
  **/
  @ApiModelProperty(example = "null", value = "Output parameter definitions for the execution. **Optional**")
  public List<ServicesInputParameterDefinitions> getOutputParameterDefinitions() {
    return outputParameterDefinitions;
  }

  public void setOutputParameterDefinitions(List<ServicesInputParameterDefinitions> outputParameterDefinitions) {
    this.outputParameterDefinitions = outputParameterDefinitions;
  }

  public PublishRequest1 runtimeType(RuntimeTypeEnum runtimeType) {
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

  public PublishRequest1 initCode(String initCode) {
    this.initCode = initCode;
    return this;
  }

   /**
   * Code that runs before each request. Specific to the runtime type. **Optional**
   * @return initCode
  **/
  @ApiModelProperty(example = "null", value = "Code that runs before each request. Specific to the runtime type. **Optional**")
  public String getInitCode() {
    return initCode;
  }

  public void setInitCode(String initCode) {
    this.initCode = initCode;
  }

  public PublishRequest1 outputFileNames(List<String> outputFileNames) {
    this.outputFileNames = outputFileNames;
    return this;
  }

  public PublishRequest1 addOutputFileNamesItem(String outputFileNamesItem) {
    this.outputFileNames.add(outputFileNamesItem);
    return this;
  }

   /**
   * Files that are returned by the response. **Optional**
   * @return outputFileNames
  **/
  @ApiModelProperty(example = "null", value = "Files that are returned by the response. **Optional**")
  public List<String> getOutputFileNames() {
    return outputFileNames;
  }

  public void setOutputFileNames(List<String> outputFileNames) {
    this.outputFileNames = outputFileNames;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PublishRequest1 publishRequest1 = (PublishRequest1) o;
    return Objects.equals(this.snapshotId, publishRequest1.snapshotId) &&
        Objects.equals(this.code, publishRequest1.code) &&
        Objects.equals(this.description, publishRequest1.description) &&
        Objects.equals(this.operationId, publishRequest1.operationId) &&
        Objects.equals(this.inputParameterDefinitions, publishRequest1.inputParameterDefinitions) &&
        Objects.equals(this.outputParameterDefinitions, publishRequest1.outputParameterDefinitions) &&
        Objects.equals(this.runtimeType, publishRequest1.runtimeType) &&
        Objects.equals(this.initCode, publishRequest1.initCode) &&
        Objects.equals(this.outputFileNames, publishRequest1.outputFileNames);
  }

  @Override
  public int hashCode() {
    return Objects.hash(snapshotId, code, description, operationId, inputParameterDefinitions, outputParameterDefinitions, runtimeType, initCode, outputFileNames);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PublishRequest1 {\n");
    
    sb.append("    snapshotId: ").append(toIndentedString(snapshotId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
    sb.append("    inputParameterDefinitions: ").append(toIndentedString(inputParameterDefinitions)).append("\n");
    sb.append("    outputParameterDefinitions: ").append(toIndentedString(outputParameterDefinitions)).append("\n");
    sb.append("    runtimeType: ").append(toIndentedString(runtimeType)).append("\n");
    sb.append("    initCode: ").append(toIndentedString(initCode)).append("\n");
    sb.append("    outputFileNames: ").append(toIndentedString(outputFileNames)).append("\n");
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

