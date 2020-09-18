package com.thoughtworks.rslist.api;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class Vote {

  private Integer id;

  @NotNull
  private Integer researchId;

  @NotNull
  @Min(1)
  @Max(10)
  private Integer voteNum;

  @NotNull
  private Integer userId;

  @NotEmpty
  private LocalDateTime voteTime;
}
