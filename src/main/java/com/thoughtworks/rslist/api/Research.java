package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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

  @JsonProperty
  public User getUser() {
    return user;
  }

  @JsonIgnore
  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return name + "  " + keyword;
  }
}
