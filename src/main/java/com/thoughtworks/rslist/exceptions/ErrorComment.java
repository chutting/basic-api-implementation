package com.thoughtworks.rslist.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorComment {
  private String error;
}
