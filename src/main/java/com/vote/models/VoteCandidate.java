package com.vote.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(
  name = "vote_candidate",
  indexes = {@Index(name = "IDX_VOTE_CANDIDATE", columnList = "create_dt, candidate_id")}
)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class VoteCandidate {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @ManyToOne private Vote vote;

  @ManyToOne private Candidate candidate;

  @Column(name = "create_dt")
  private LocalDateTime createDt;

  public Vote getVote() {
    return vote;
  }

  public void setVote(Vote vote) {
    this.vote = vote;
  }

  public Candidate getCandidate() {
    return candidate;
  }

  public void setCandidate(Candidate candidate) {
    this.candidate = candidate;
  }

  public LocalDateTime getCreateDt() {
    return createDt;
  }

  public void setCreateDt(LocalDateTime createDt) {
    this.createDt = createDt;
  }
}
