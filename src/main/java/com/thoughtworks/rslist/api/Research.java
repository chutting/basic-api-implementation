package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Research {
//  public interface WithoutUserView {};
//  public interface WithUserView extends WithoutUserView {};

  @NotEmpty
//  @JsonView(WithoutUserView.class)
  private String name;

  @NotEmpty
//  @JsonView(WithoutUserView.class)
  private String keyword;

  @NotNull
//  @JsonView(WithUserView.class)
  private @Valid User user;

  @JsonIgnore
  public User getUser() {
    return user;
  }

  @JsonProperty
  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return name + "  " + keyword;
  }
}
