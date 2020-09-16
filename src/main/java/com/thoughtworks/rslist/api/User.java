package com.thoughtworks.rslist.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @NotEmpty
  @Size(max = 8)
  private String userName;

  @NotNull
  @Range(min = 18, max = 100)
  private Integer age;

  @NotEmpty
  private String gender;
  private String email;
  private String phoneNumber;
}
