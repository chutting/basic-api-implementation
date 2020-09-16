package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ResearchSerialize extends JsonSerializer<Research> {
  @Override
  public void serialize(Research value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("name", value.getName());
    gen.writeStringField("keyword", value.getKeyword());
    gen.writeEndObject();
  }
}
