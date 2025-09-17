package com.gitscode.gitscode.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {






  // ==== Common ====
  VALIDATION_FAILED;


  @Getter
  private final HttpStatus status;

  ErrorCode() {
    this.status = HttpStatus.BAD_REQUEST;
  }

  ErrorCode(HttpStatus status) {
    this.status = status;
  }
}
