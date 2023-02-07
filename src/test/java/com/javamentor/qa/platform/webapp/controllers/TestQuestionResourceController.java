package com.javamentor.qa.platform.webapp.controllers;

import com.javamentor.qa.platform.webapp.configs.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.Query;
import java.math.BigInteger;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestQuestionResourceController extends AbstractControllerTest {

    @Test
    @Sql(scripts = "/script/TestQuestionResourceController/addQuestionToBookmarks/Before.sql",
            executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestQuestionResourceController/addQuestionToBookmarks/After.sql",
            executionPhase = AFTER_TEST_METHOD)
    public void addQuestionToBookmarks () throws Exception {
        String authUserToken = getToken("0@mail.com", "pass0");
        mockMvc.perform(post ("/api/user/question/100/bookmark").header(AUTHORIZATION, authUserToken)).andExpect(status().isOk());
        mockMvc.perform(post ("/api/user/question/101/bookmark").header(AUTHORIZATION, authUserToken)).andExpect(status().isOk());
        mockMvc.perform(post ("/api/user/question/102/bookmark").header(AUTHORIZATION, authUserToken)).andExpect(status().isOk());
        BigInteger count = (BigInteger) super.entityManager.createNativeQuery("SELECT COUNT(*) FROM bookmarks").getSingleResult();
        Assertions.assertEquals(count.intValue(), 3);
    }

    @Test
    @Sql(scripts = "/script/TestQuestionResourceController/addQuestionToBookmarksUnauthorizedUser/Before.sql",
         executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestQuestionResourceController/addQuestionToBookmarksUnauthorizedUser/After.sql",
         executionPhase = AFTER_TEST_METHOD)
    public void addQuestionToBookmarksUnauthorizedUser() throws Exception {
        Assertions.assertEquals(1, 1);
        mockMvc.perform(post ("/api/user/question/100/bookmark"));
        mockMvc.perform(post ("/api/user/question/101/bookmark"));
        mockMvc.perform(post ("/api/user/question/102/bookmark"));
        BigInteger count = (BigInteger) super.entityManager.createNativeQuery("SELECT COUNT(*) FROM bookmarks").getSingleResult();
        Assertions.assertEquals(count.intValue(), 0);
    }

    @Test
    @Sql(scripts = "/script/TestQuestionResourceController/addNonExistedQuestionToBookmarks/Before.sql",
            executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestQuestionResourceController/addNonExistedQuestionToBookmarks/After.sql",
            executionPhase = AFTER_TEST_METHOD)
    public void addNonExistedQuestionToBookmarks() throws Exception {
        String authUserToken = getToken("0@mail.com", "pass0");
        mockMvc.perform(post ("/api/user/question/100/bookmark").header(AUTHORIZATION, authUserToken)).andExpect(status().is4xxClientError());
        mockMvc.perform(post ("/api/user/question/101/bookmark").header(AUTHORIZATION, authUserToken)).andExpect(status().is4xxClientError());
        mockMvc.perform(post ("/api/user/question/102/bookmark").header(AUTHORIZATION, authUserToken)).andExpect(status().is4xxClientError());
        BigInteger count = (BigInteger) super.entityManager.createNativeQuery("SELECT COUNT(*) FROM bookmarks").getSingleResult();
        Assertions.assertEquals(count.intValue(), 0);
    }

    @Test
    @Sql(scripts = "/script/TestQuestionResourceController/addQuestionIsAlreadyBookmarked/Before.sql",
            executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/script/TestQuestionResourceController/addQuestionIsAlreadyBookmarked/After.sql",
            executionPhase = AFTER_TEST_METHOD)
    public void addQuestionIsAlreadyBookmarked() throws Exception {
        String authUserToken = getToken("0@mail.com", "pass0");
        mockMvc.perform(post ("/api/user/question/100/bookmark").header(AUTHORIZATION, authUserToken)).andExpect(status().is2xxSuccessful());
        mockMvc.perform(post ("/api/user/question/100/bookmark").header(AUTHORIZATION, authUserToken)).andExpect(status().is2xxSuccessful());
        BigInteger count = (BigInteger) super.entityManager.createNativeQuery("SELECT COUNT(*) FROM bookmarks").getSingleResult();
        Assertions.assertEquals(count.intValue(), 1);
    }

}
