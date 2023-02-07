package com.javamentor.qa.platform.search;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchQuestionInputParam {
    private final Map<String, Object> keyWords = new HashMap<>();
    private final List<String> listWords = new ArrayList<>();

    public SearchQuestionInputParam() {
    }

    public Map<String, Object> getKeyWords() {
        keyWords.put("user", "User:");
        keyWords.put("body", "Body:");
        keyWords.put("title", "Title:");
        keyWords.put("tag", "^[\\[](.*)[\\]]$");
        keyWords.put("fullMatch", "^[\"](.*)[\"]$");
        return keyWords;
    }

    public List<String> getListWords(String request) {
        for (String oneWord : request.split("\\s")) {
            listWords.add(oneWord);
        }
        return listWords;
    }
}