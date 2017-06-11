package com.vote.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import com.vote.models.*;
import com.vote.services.VoteService;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VoteServiceTests {

  public static final String VOTER_ID = "A";
  public static final String CANDIDATE_ID = "A";

  private CandidateRepository candidateRepository;

  private VoteRepository voteRepository;

  private VoteCandidateRepository voteCandidateRepository;

  private VoteService voteService;

  @Before
  public void setUp() {

    candidateRepository = Mockito.mock(CandidateRepository.class);
    voteRepository = Mockito.mock(VoteRepository.class);
    voteCandidateRepository = Mockito.mock(VoteCandidateRepository.class);

    voteService = new VoteService(voteRepository, candidateRepository, voteCandidateRepository);
  }

  @Test
  public void shouldRegisterAVoteForTheCandidate() {
    ;
    Vote vote = new Vote();
    vote.setVoterId(VOTER_ID);

    when(voteRepository.findByVoterId(eq(VOTER_ID))).thenReturn(Optional.of(vote));

    Candidate candidate = new Candidate();
    candidate.setCandidate(CANDIDATE_ID);
    when(candidateRepository.findByCandidate(eq(VOTER_ID))).thenReturn(Optional.of(candidate));

    doAnswer(returnsFirstArg()).when(voteRepository).save(any(Vote.class));
    doAnswer(returnsFirstArg()).when(candidateRepository).save(any(Candidate.class));
    doAnswer(returnsFirstArg()).when(voteCandidateRepository).save(any(VoteCandidate.class));

    HttpStatus status = voteService.registerAndVote(VOTER_ID, CANDIDATE_ID);
    assertThat(status.value()).isEqualTo(201);
  }

  @Test
  public void shouldAllowMaxOfThreeVotes() {

    Vote vote = new Vote();
    vote.setVoterId(VOTER_ID);

    VoteCandidate vote1 = new VoteCandidate();
    VoteCandidate vote2 = new VoteCandidate();
    VoteCandidate vote3 = new VoteCandidate();

    vote.getVoteCandidates().add(vote1);
    vote.getVoteCandidates().add(vote2);
    vote.getVoteCandidates().add(vote3);

    when(voteRepository.findByVoterId(eq(VOTER_ID))).thenReturn(Optional.of(vote));
    HttpStatus status = voteService.registerAndVote("A", VOTER_ID);
    assertThat(status.value()).isEqualTo(403);
  }
}
