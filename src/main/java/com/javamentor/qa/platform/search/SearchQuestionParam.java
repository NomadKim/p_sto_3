package com.javamentor.qa.platform.search;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class SearchQuestionParam {
    private SearchQuestionInputParam listWordsParam = new SearchQuestionInputParam();
    private SearchQuestionInputParam keyWordsParam = new SearchQuestionInputParam();

    public SearchQuestionParam() {
    }

    public Map<String, Object> getAllParam(String request) {
        List<String> listWords = listWordsParam.getListWords(request);
        Map<String, Object> keyWords = keyWordsParam.getKeyWords();
        SearchQuestionOutputParam searchQuestionOutputParam = new SearchQuestionOutputParam(listWords, keyWords);
        return searchQuestionOutputParam.getOutputParam();
    }
}