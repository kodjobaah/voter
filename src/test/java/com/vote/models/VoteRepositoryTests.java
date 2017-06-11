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
public class VoteRepositoryTests {

  String VOTER_ID = "A";

  @Autowired private TestEntityManager entityManager;

  @Autowired private VoteRepository voteRepository;

  @Test
  public void storeVote() throws Exception {

    Vote vote = new Vote();
    this.entityManager.persist(vote);
    Vote found = this.voteRepository.findAll().iterator().next();
    assertThat(found).isNotNull();
  }

  @Test
  public void testFindByVoterId() throws Exception {

    Vote vote = new Vote();
    vote.setVoterId(VOTER_ID);
    this.entityManager.persist(vote);
    Vote found = this.voteRepository.findByVoterId(VOTER_ID).get();
    assertThat(found.getVoterId()).isEqualTo(VOTER_ID);
  }
}
