package com.thoughtworks.rslist.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @NotEmpty
  @Size(max = 8)
  private String userName;

  private Integer age;
  private String gender;
  private String email;
  private String phoneNumber;
}
