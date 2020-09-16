package com.thoughtworks.rslist.api;

import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class Research {
  private String name;
  private String keyword;
  private User user;

  public Research() {
  }

  public Research(String name, String keyword) {
    this.name = name;
    this.keyword = keyword;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  @Override
  public String toString() {
    return name + "  " + keyword;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Research research = (Research) o;
    return Objects.equals(name, research.name) &&
        Objects.equals(keyword, research.keyword);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, keyword);
  }
}
