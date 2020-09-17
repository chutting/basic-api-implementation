package com.thoughtworks.rslist.entity;

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
import javax.persistence.Table;

@Entity
@Data
@Table(name = "vote")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class VoteEntity {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(name = "research_id")
  private Integer rsEventId;

  @Column(name = "vote_num")
  private Integer voteNum;

  @JoinColumn(name = "user_id")
  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "vote_time")
  private String voteTime;
}
