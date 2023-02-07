package com.javamentor.qa.platform.webapp.controllers;

import com.javamentor.qa.platform.webapp.configs.AbstractControllerTest;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestMessageResourceController extends AbstractControllerTest {

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithOutTextParam/Before.sql"})
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithOutTextParam/After.sql"})
    public void findMessagesInGlobalChatPaginationWithOutTextParam() throws Exception {

        String userToken = getToken("0@mail.com", "pass0");

        mockMvc.perform(get("/api/user/message/global/find?currentPage=1&items=10")
                        .header(AUTHORIZATION, userToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithOutPageParam/Before.sql"})
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithOutPageParam/After.sql"})
    public void findMessagesInGlobalChatPaginationWithOutPageParam() throws Exception {

        String userToken = getToken("0@mail.com", "pass0");

        mockMvc.perform(get("/api/user/message/global/find?text=text&items=10")
                        .header(AUTHORIZATION, userToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithOutItemsParam/Before.sql"})
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithOutItemsParam/After.sql"})
    public void findMessagesInGlobalChatPaginationWithOutItemsParam() throws Exception {

        String userToken = getToken("0@mail.com", "pass0");

        mockMvc.perform(get("/api/user/message/global/find?text=text&currentPage=1")
                        .header(AUTHORIZATION, userToken))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithPage2Items1/Before.sql"})
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithPage2Items1/After.sql"})
    public void findMessagesInGlobalChatPaginationWithPage2Items1() throws Exception {

        String userToken = getToken("0@mail.com", "pass0");

        mockMvc.perform(get("/api/user/message/global/find?text=message&currentPage=2&items=1")
                        .header(AUTHORIZATION, userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", Is.is(2)))
                .andExpect(jsonPath("$.totalPageCount", Is.is(22)))
                .andExpect(jsonPath("$.totalResultCount", Is.is(22)))
                .andExpect(jsonPath("$.items.length()", Is.is(1)))
                .andExpect(jsonPath("$.items[0].id", Is.is(101)))
                .andExpect(jsonPath("$.items[0].message", Is.is("message 101")))
                .andExpect(jsonPath("$.items[0].userId", Is.is(100)))
                .andExpect(jsonPath("$.itemsOnPage", Is.is(1)));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithPage1Items50/Before.sql"})
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            scripts = {"/script/TestMessageResourceController/findMessagesInGlobalChatPaginationWithPage1Items50/After.sql"})
    public void findMessagesInGlobalChatPaginationWithPage1Items50() throws Exception {

        String userToken = getToken("0@mail.com", "pass0");

        mockMvc.perform(get("/api/user/message/global/find?text=message&currentPage=1&items=50")
                        .header(AUTHORIZATION, userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPageNumber", Is.is(1)))
                .andExpect(jsonPath("$.totalPageCount", Is.is(1)))
                .andExpect(jsonPath("$.totalResultCount", Is.is(22)))
                .andExpect(jsonPath("$.items.length()", Is.is(22)))
                .andExpect(jsonPath("$.items[0].id", Is.is(100)))
                .andExpect(jsonPath("$.items[0].message", Is.is("message 100")))
                .andExpect(jsonPath("$.items[0].userId", Is.is(100)))
                .andExpect(jsonPath("$.itemsOnPage", Is.is(50)));
    }
}
