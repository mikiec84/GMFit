package com.mcsaatchi.gmfit.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TakenMedicalTestsResponseData {
  @SerializedName("message") @Expose private String message;
  @SerializedName("body") @Expose private List<TakenMedicalTestsResponseBody> body =
      new ArrayList<>();
  @SerializedName("code") @Expose private Integer code;

  /**
   * @return The message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message The message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return The body
   */
  public List<TakenMedicalTestsResponseBody> getBody() {
    return body;
  }

  /**
   * @param body The body
   */
  public void setBody(List<TakenMedicalTestsResponseBody> body) {
    this.body = body;
  }

  /**
   * @return The code
   */
  public Integer getCode() {
    return code;
  }

  /**
   * @param code The code
   */
  public void setCode(Integer code) {
    this.code = code;
  }
}
