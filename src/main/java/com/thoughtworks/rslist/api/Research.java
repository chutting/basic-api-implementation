package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonSerialize(using = ResearchSerialize.class)
@JsonDeserialize(using = ResearchDeserialize.class)
public class Research {
  private String name;
  private String keyword;

//  @JsonSerialize(using = ResearchSerialize.class)
  private @Valid User user;

//  @JsonIgnore
  public User getUser() {
    return user;
  }

//  @JsonProperty("userInfo")
  public void setUser(User user) {
    this.user = user;
  }

  public Research(String name, String keyword) {
    this.name = name;
    this.keyword = keyword;
  }

  @Override
  public String toString() {
    return name + "  " + keyword;
  }
}
