package com.vote.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.TestRepositoryConfig;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRepositoryConfig.class)
@DataJpaTest
@Transactional
public class VoteCandidateRepositoryTests {

  class DataKey {

    private String value;

    private String uuid = UUID.randomUUID().toString();

    public DataKey(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }

    public String getId() {
      return this.uuid;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj != null && obj instanceof DataKey) {
        DataKey s = (DataKey) obj;
        return uuid.equals(s.uuid);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return uuid.hashCode();
    }
  }

  @Autowired private TestEntityManager entityManager;

  @Autowired private VoteCandidateRepository voteCandidateRepository;

  @Autowired JdbcTemplate jdbcTemplate;

  @Test
  public void storeVoteCandidate() throws Exception {

    LocalDateTime date = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    VoteCandidate voteCandidate = new VoteCandidate();
    voteCandidate.setCreateDt(LocalDateTime.now());
    this.entityManager.persist(voteCandidate);
    VoteCandidate found = this.voteCandidateRepository.findAll().iterator().next();
    assertThat(found.getCreateDt().format(formatter)).isEqualTo(date.format(formatter));
  }

  @Test
  public void testFindVoteCountWhenNoVotes() throws Exception {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String testDate1 = "2016-08-09 10:30:10";

    LocalDateTime td1 = LocalDateTime.parse(testDate1, formatter);

    int count = this.voteCandidateRepository.findVoteCount(td1, td1).size();

    assertThat(count).isEqualTo(Integer.valueOf(0));
  }

  @Test
  public void testFindVoteCountForSingle() throws Exception {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String testDate1 = "2016-08-09 10:30:10";

    LocalDateTime td1 = LocalDateTime.parse(testDate1, formatter);
    Map<DataKey, LocalDateTime> testData = new HashMap<DataKey, LocalDateTime>();
    testData.put(new DataKey("A"), td1);

    generateFindCountData(testData);

    List<VoteResults> voteResults = this.voteCandidateRepository.findVoteCount(td1, td1);

    assertThat(voteResults.size()).isEqualTo(Integer.valueOf(1));

    VoteResults vr2 =
        voteResults.stream().filter(vr -> vr.getCandidate().equals("A")).findFirst().get();
    assertThat(vr2.getVotes()).isEqualTo(1);
  }

  @Test
  public void testFindVoteCountForMultiple() throws Exception {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String testDate1 = "2016-08-09 10:30:10";
    String testDate2 = "2016-09-09 10:30:10";
    String testDate3 = "2016-10-09 10:30:10";

    LocalDateTime td1 = LocalDateTime.parse(testDate1, formatter);
    LocalDateTime td2 = LocalDateTime.parse(testDate2, formatter);
    LocalDateTime td3 = LocalDateTime.parse(testDate3, formatter);

    Map<DataKey, LocalDateTime> testData = new HashMap<DataKey, LocalDateTime>();
    testData.put(new DataKey("A"), td1);
    testData.put(new DataKey("B"), td2);
    testData.put(new DataKey("C"), td3);
    testData.put(new DataKey("D"), td3);
    testData.put(new DataKey("D"), td3);

    generateFindCountData(testData);

    List<VoteResults> voteResults = this.voteCandidateRepository.findVoteCount(td2, td3);

    assertThat(voteResults.size()).isEqualTo(Integer.valueOf(3));

    VoteResults vr2 =
        voteResults.stream().filter(vr -> vr.getCandidate().equals("B")).findFirst().get();
    assertThat(vr2.getVotes()).isEqualTo(1);
    VoteResults vr3 =
        voteResults.stream().filter(vr -> vr.getCandidate().equals("C")).findFirst().get();
    assertThat(vr3.getVotes()).isEqualTo(1);
    VoteResults vr4 =
        voteResults.stream().filter(vr -> vr.getCandidate().equals("D")).findFirst().get();
    assertThat(vr4.getVotes()).isEqualTo(2);
  }

  private void generateFindCountData(Map<DataKey, LocalDateTime> testData) {

    testData
        .entrySet()
        .stream()
        .forEach(
            entry -> {
              Candidate candidate = new Candidate();

              Optional<Long> candId = checkIfCandExist(entry.getKey().getValue());

              if (candId.isPresent()) {
                candidate = this.entityManager.find(Candidate.class, candId.get());
              } else {
                candidate.setCandidate(entry.getKey().getValue());
              }
              this.entityManager.persist(candidate);

              VoteCandidate voteCandidate = new VoteCandidate();
              voteCandidate.setCreateDt(entry.getValue());
              voteCandidate.setCandidate(candidate);

              Vote vote = new Vote();
              vote.setVoterId(entry.getKey().getId());
              vote.addVoteCandidate(voteCandidate);
              voteCandidate.setVote(vote);
              this.entityManager.persist(vote);
              this.entityManager.persist(voteCandidate);
              this.entityManager.flush();
            });
  }

  public Optional<Long> checkIfCandExist(String candId) {
    String sql = String.format("SELECT id FROM Candidate c where c.candidate_id = '%s' ", candId);

    try {
      Long id = jdbcTemplate.queryForObject(sql, Long.class);

      return Optional.of(id);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
