package com.vote.models;

public class VoteResults {
  private String candidate;
  private Long votes;

  public VoteResults() {}

  public VoteResults(Candidate candidate, Long votes) {
    this.candidate = candidate.getCandidate();
    this.votes = votes;
  }

  public String getCandidate() {
    return candidate;
  }

  public void setCandidate(String candidate) {
    this.candidate = candidate;
  }

  public Long getVotes() {
    return votes;
  }

  public void setVotes(Long votes) {
    this.votes = votes;
  }
}
