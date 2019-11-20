package com.jitterted.jittershout.adapter.triggering.api;

import com.jitterted.jittershout.domain.Shouter;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@WebMvcTest(controllers = BotController.class)
@Import(FakeConfig.class)
@Tag("integration")
public class BotInfoIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Shouter shouter;

  @Test
  public void getBotInfoReturnsShoutOutStateJson() throws Exception {
    shouter.changeShoutOutActiveTo(true);
    mockMvc.perform(get("/api/botinfo")
                        .accept(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.shoutOutActive", is(true)))
           .andExpect(jsonPath("$.shoutOutCount", is(3)));
  }

  @Test
  public void postBotInfoWithActiveAsFalseChangesShouterToInactive() throws Exception {
    shouter.changeShoutOutActiveTo(true);
    mockMvc.perform(post("/api/botinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                     {"shoutOutActive": false}
                                     """))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.shoutOutActive", is(false)));

    assertThat(shouter.isShoutOutActive())
        .isFalse();
  }
}
