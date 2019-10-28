package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.domain.TwitchTeam;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeamInfoIntegrationTest {

  @MockBean
  private TwitchTeam twitchTeam;

  @Test
  public void getOfTeamInfoReturnsValidTeamInfoJson(@Autowired MockMvc mockMvc) throws Exception {
    Mockito.when(twitchTeam.name()).thenReturn("team name");
    Mockito.when(twitchTeam.count()).thenReturn(91);

    mockMvc.perform(get("/api/teaminfo")
                        .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is("team name")))
           .andExpect(jsonPath("$.count", is("91")));
  }
}
