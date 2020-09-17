package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private Integer id;

  public User(@NotEmpty @Size(max = 8) String userName, @NotNull @Range(min = 18, max = 100) Integer age, @NotEmpty String gender, @Email String email, @NotEmpty @Pattern(regexp = "^1\\d{10}") String phoneNumber) {
    this.userName = userName;
    this.age = age;
    this.gender = gender;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  @NotEmpty
  @Size(max = 8)
  @JsonProperty("user_name")
  private String userName;

  @NotNull
  @Range(min = 18, max = 100)
  @JsonProperty("user_age")
  private Integer age;

  @NotEmpty
  @JsonProperty("user_gender")
  private String gender;

  @Email
  @JsonProperty("user_email")
  private String email;

  @NotEmpty
  @Pattern(regexp = "^1\\d{10}")
  @JsonProperty("user_phone")
  private String phoneNumber;

  @NotNull
  @Min(0)
  @Max(10)
  private Integer voteNum = 10;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(userName, user.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName);
  }
}
