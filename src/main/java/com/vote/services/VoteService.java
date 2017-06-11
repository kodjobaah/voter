package com.vote.services;

import com.vote.models.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {

  private static final Logger logger = LoggerFactory.getLogger(VoteService.class);
  public static final int MAX_NUMBER_OF_VOTES = 3;

  ReentrantLock lock = new ReentrantLock();

  @Autowired private CandidateRepository candidateRepository;

  @Autowired private VoteRepository voteRepository;

  @Autowired private VoteCandidateRepository voteCandidateRepository;

  public VoteService() {}

  public VoteService(
      VoteRepository voteRepository,
      CandidateRepository candidateRepository,
      VoteCandidateRepository voteCandidateRepository) {

    this.voteRepository = voteRepository;
    this.candidateRepository = candidateRepository;
    this.voteCandidateRepository = voteCandidateRepository;
  }

  @Transactional
  public HttpStatus registerAndVote(final String voter, final String candidate) {

    //To prevent a user voting more than three times
    lock.lock();

    try {
      Vote actualVote =
          voteRepository
              .findByVoterId(voter)
              .orElseGet(
                  () -> {
                    Vote vote = new Vote();
                    vote.setVoterId(voter);
                    return vote;
                  });

      if (actualVote.getVoteCandidates().size() == MAX_NUMBER_OF_VOTES) {
        logger.error(
            "unable to vote voter = [{}]  voteId = [{}]  voteCount = [{} ]execeed max",
            voter,
            actualVote.getId(),
            actualVote.getVoteCandidates().size());
        return HttpStatus.FORBIDDEN;
      }

      Candidate actualCandidate =
          candidateRepository
              .findByCandidate(candidate)
              .orElseGet(
                  () -> {
                    Candidate theCandidate = new Candidate();
                    theCandidate.setCandidate(candidate);
                    return theCandidate;
                  });

      VoteCandidate voteCandidate = new VoteCandidate();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);

      voteCandidate.setCreateDt(now);

      actualVote.addVoteCandidate(voteCandidate);
      voteCandidate.setVote(actualVote);

      actualCandidate.addVoteCandidate(voteCandidate);
      voteCandidate.setCandidate(actualCandidate);

      voteRepository.save(actualVote);
      candidateRepository.save(actualCandidate);
      voteCandidateRepository.save(voteCandidate);

    } finally {
      lock.unlock();
    }
    return HttpStatus.CREATED;
  }
}
