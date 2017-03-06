package com.mcsaatchi.gmfit.architecture.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CRMCategoriesResponseData {
  @SerializedName("message") @Expose private String message;
  @SerializedName("body") @Expose private CRMCategoriesResponseBody body;
  @SerializedName("code") @Expose private Integer code;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public CRMCategoriesResponseBody getBody() {
    return body;
  }

  public void setBody(CRMCategoriesResponseBody body) {
    this.body = body;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }
}
