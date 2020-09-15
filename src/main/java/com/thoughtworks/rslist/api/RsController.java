package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<Research> rsList = Arrays.asList(
      new Research("第一条事件", "经济"),
      new Research("第二条事件", "政治"),
      new Research("第三条事件", "娱乐"));

  @GetMapping("/rs/list")
  public String getRsList(@RequestParam(required = false) Integer start,
                          @RequestParam(required = false) Integer end) {
    start = (start == null ? 1 : start);
    end = (end == null ? rsList.size() : end);

    StringBuilder result = new StringBuilder();
    List<Research> outputRsList = rsList.subList(start - 1, end);

    for (int index = 0; index < outputRsList.size(); index++) {
      result.append(index + 1).append("  ").append(outputRsList.get(index).toString()).append("\n");
    }

    return String.valueOf(result);
  }

  @GetMapping("/rs/findByIndex/{index}")
  public Research getResearchByIndex(@PathVariable int index) {
    return rsList.get(index - 1);
  }


}
