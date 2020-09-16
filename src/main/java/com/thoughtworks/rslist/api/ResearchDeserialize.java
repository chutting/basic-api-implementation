package com.thoughtworks.rslist.api;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class ResearchDeserialize extends JsonDeserializer<Research> {
  @Override
  public Research deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    Root root = p.readValueAs(Root.class);
    Research research = new Research(root.name, root.keyword, root.user);
    return research;
  }
  private static class Root {
    public User user;
    public String name;
    public String keyword;
  }
}

