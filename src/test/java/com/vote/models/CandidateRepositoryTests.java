package com.vote.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.TestRepositoryConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRepositoryConfig.class)
@DataJpaTest
@Transactional
public class CandidateRepositoryTests {

  @Autowired private TestEntityManager entityManager;

  @Autowired private CandidateRepository candidateRepository;

  @Test
  public void findByCandidate() throws Exception {

    String candidateId = "candidate_id";
    Candidate candidate = new Candidate();
    candidate.setCandidate(candidateId);
    this.entityManager.persist(candidate);
    Candidate found = this.candidateRepository.findByCandidate(candidateId).get();
    assertThat(found.getCandidate()).isEqualTo(candidateId);
  }
}
