package com.javamentor.qa.platform.models.dto;

import org.hibernate.transform.ResultTransformer;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestionViewDtoResultTransformer implements ResultTransformer {


    private static final long serialVersionUID = 8909545788077515255L;
    private final transient Map<Long, QuestionViewDto> questionViewDtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {

        Long questionId = ((Number) tuple[0]).longValue();

        return questionViewDtoMap.computeIfAbsent(
                questionId,
                v -> {
                    QuestionViewDto questionViewDtoTemp = new QuestionViewDto();
                    questionViewDtoTemp.setId(((BigInteger) tuple[0]).longValue());
                    questionViewDtoTemp.setTitle((String) tuple[1]);
                    questionViewDtoTemp.setDescription((String) tuple[2]);
                    questionViewDtoTemp.setLastUpdateDateTime(((Timestamp) tuple[3]).toLocalDateTime());
                    questionViewDtoTemp.setPersistDateTime(((Timestamp) tuple[4]).toLocalDateTime());
                    questionViewDtoTemp.setAuthorId(((BigInteger) tuple[5]).longValue());
                    questionViewDtoTemp.setAuthorName((String) tuple[6]);
                    questionViewDtoTemp.setAuthorImage((String) tuple[7]);
                    questionViewDtoTemp.setAuthorReputation(((BigInteger) tuple[8]).longValue());
                    questionViewDtoTemp.setCountValuable(((BigInteger) tuple[9]).intValue());
                    questionViewDtoTemp.setCountAnswer(((BigInteger) tuple[10]).intValue());
                    questionViewDtoTemp.setViewCount(((BigInteger) tuple[11]).intValue());
                    questionViewDtoTemp.setListTagDto(new ArrayList<>());
                    questionViewDtoTemp.setIsUserBookMarks(tuple[12].equals(new BigInteger("1")));

                    return questionViewDtoTemp;
                }
        );
    }

    @Override
    public List<QuestionViewDto> transformList(List list) {
        return new ArrayList<>(questionViewDtoMap.values());
    }
}
