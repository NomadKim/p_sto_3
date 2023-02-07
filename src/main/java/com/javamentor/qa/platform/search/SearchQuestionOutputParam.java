package com.javamentor.qa.platform.search;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SearchQuestionOutputParam {
    private final List<String> listWords;
    private final Map<String, Object> keyWords;
    private final Map<String, Object> outputParam = new HashMap<>();
    private Matcher matcher;
    private Pattern pattern;

    public SearchQuestionOutputParam(List<String> listWord, Map<String, Object> keyWords) {
        this.listWords = listWord;
        this.keyWords = keyWords;
    }

    public Map<String, Object> getOutputParam() {
        for (String keyWord : keyWords.keySet()){
            pattern = Pattern.compile((String) keyWords.get(keyWord));
            for (Iterator<String> listWordIterator = listWords.listIterator();
                 listWordIterator.hasNext();) {
                String nextListWord = listWordIterator.next();
                matcher = pattern.matcher(nextListWord);
                if (matcher.find()) {
                    if(((String) keyWords.get(keyWord)).contains("*")) {
                        outputParam.put(keyWord, nextListWord.substring(1, nextListWord.length()-1));
                    } else {
                        outputParam.put(keyWord, nextListWord.substring(matcher.end()));
                    }
                    listWordIterator.remove();
                }
            }
        }
        for (String word : listWords) {
            outputParam.merge("request", word, (a,b) -> a + " " + b);
        }
        listWords.clear();
        return outputParam;
    }
}