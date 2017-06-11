package com.vote.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vote.models.VoteResults;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VoteControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void countMeUpAcceptsAVote() throws Exception {
    String userId = "A" + UUID.randomUUID().toString();
    String request1 = "/countMeUp/candidate/A/voter/" + userId;
    this.mockMvc.perform(post(request1)).andExpect(status().isCreated());
  }

  @Test
  public void countMeUpOnlyAcceptsThreeVotesPerUser() throws Exception {
    String userId = "B" + UUID.randomUUID().toString();
    String request1 = "/countMeUp/candidate/A/voter/" + userId;
    this.mockMvc.perform(post(request1)).andExpect(status().isCreated());
    this.mockMvc.perform(post(request1)).andExpect(status().isCreated());
    this.mockMvc.perform(post(request1)).andExpect(status().isCreated());

    String request2 = "/countMeUp/candidate/B/voter/" + userId;
    this.mockMvc.perform(post(request2)).andExpect(status().is(403));
  }

  @Test
  public void countMeUpOnlyAcceptsThreeVotesPerUserRegardlessOfCandidate() throws Exception {
    String userId = "C" + UUID.randomUUID().toString();
    String request1 = "/countMeUp/candidate/A/voter/" + userId;
    this.mockMvc.perform(post(request1)).andExpect(status().isCreated());
    this.mockMvc.perform(post(request1)).andExpect(status().isCreated());

    String request2 = "/countMeUp/candidate/D/voter/" + userId;
    this.mockMvc.perform(post(request2)).andExpect(status().isCreated());

    String request3 = "/countMeUp/candidate/B/voter/" + userId;
    this.mockMvc.perform(post(request3)).andExpect(status().is(403));
  }

  @Test
  public void countMeUpWithInvalidCandidate() throws Exception {
    String userId = "C" + UUID.randomUUID().toString();
    String request1 = "/countMeUp/candidate/E/voter/" + userId;
    this.mockMvc.perform(post(request1)).andExpect(status().isBadRequest());
  }

  @Test
  public void countMeUpResults() throws Exception {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String userId = "B" + UUID.randomUUID().toString();
    String request1 = "/countMeUp/candidate/A/voter/" + userId;
    this.mockMvc.perform(post(request1));

    String t = now.format(formatter);
    String getCount = String.format("/countMeUp/results/start/%s/end/%s", t, t);
    VoteResults[] resp = this.restTemplate.getForObject(getCount, VoteResults[].class);
    List<VoteResults> response = Arrays.asList(resp);
    assertThat(response.size()).isEqualTo(1);
    VoteResults vs = response.get(0);

    assertThat(vs.getVotes()).isEqualTo(1L);
    assertThat(vs.getCandidate()).isEqualTo("A");
  }

  @Test
  public void countMeUpResultsWithInvalidDate() throws Exception {
    String getCount = String.format("/countMeUp/results/start/notADate/end/notADate");
    this.mockMvc.perform(get(getCount)).andExpect(status().is(400));
  }
}
