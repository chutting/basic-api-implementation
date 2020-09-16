package com.thoughtworks.rslist.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Research {
  private String name;
  private String keyword;
  @Valid private User user;

  public Research(String name, String keyword) {
    this.name = name;
    this.keyword = keyword;
  }

  @Override
  public String toString() {
    return name + "  " + keyword;
  }
}
