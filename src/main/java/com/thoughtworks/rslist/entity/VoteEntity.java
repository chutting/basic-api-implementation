package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "vote")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteEntity {

  @Id
  @GeneratedValue
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "research_id")
  private ResearchEntity research;

  @Column(name = "vote_num")
  private Integer voteNum;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "vote_time")
  private LocalDateTime voteTime;

  @JsonIgnore
  public ResearchEntity getResearch() {
    return research;
  }

  @JsonProperty
  public void setResearch(ResearchEntity research) {
    this.research = research;
  }
}
