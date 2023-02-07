package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.webapp.configs.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigInteger;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestChatResourceController extends AbstractControllerTest {

    @Test
    @Sql(scripts = "/script/TestChatResourceController/joinGroupChat/Before.sql",
            executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/joinGroupChat/After.sql",
            executionPhase = AFTER_TEST_METHOD)
    public void joinGroupChatDoesUserExist() throws Exception {
        String token = super.getToken("0@mail.com", "pass0");
        mockMvc.perform(
                        post("/api/user/chat/group/100/join")
                                .header(AUTHORIZATION, token)
                                .content("101"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "/script/TestChatResourceController/joinGroupChat/Before.sql",
            executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/joinGroupChat/After.sql",
            executionPhase = AFTER_TEST_METHOD)
    public void joinGroupChatDoesChatExist() throws Exception {
        String token = super.getToken("0@mail.com", "pass0");
        mockMvc.perform(
                        post("/api/user/chat/group/101/join")
                                .header(AUTHORIZATION, token)
                                .content("100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "/script/TestChatResourceController/joinGroupChat/Before.sql",
            executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/joinGroupChat/After.sql",
            executionPhase = AFTER_TEST_METHOD)
    public void joinGroupChatIsUserAlreadyJoined() throws Exception {
        String token = super.getToken("0@mail.com", "pass0");
        mockMvc.perform(
                        post("/api/user/chat/group/100/join")
                                .header(AUTHORIZATION, token)
                                .content("100"))
                .andExpect(status().isOk());
        mockMvc.perform(
                        post("/api/user/chat/group/100/join")
                                .header(AUTHORIZATION, token)
                                .content("100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "/script/TestChatResourceController/joinGroupChat/Before.sql",
            executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestChatResourceController/joinGroupChat/After.sql",
            executionPhase = AFTER_TEST_METHOD)
    public void joinGroupChatJoinUserAndConfirm() throws Exception {
        String token = super.getToken("0@mail.com", "pass0");
        mockMvc.perform(
                        post("/api/user/chat/group/100/join")
                                .header(AUTHORIZATION, token)
                                .content("100"))
                .andExpect(status().isOk());
        Assertions.assertEquals(BigInteger.valueOf(100),
                entityManager
                        .createNativeQuery("SELECT user_id FROM groupchat_has_users WHERE chat_id = 100")
                        .getSingleResult()
        );
    }
}
