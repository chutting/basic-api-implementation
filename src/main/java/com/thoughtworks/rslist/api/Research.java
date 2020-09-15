package com.thoughtworks.rslist.api;

public class Research {
  private String name;
  private String keyword;

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
}
