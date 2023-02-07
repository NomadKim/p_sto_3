package com.javamentor.qa.platform.models.entity.question.answer;

public enum VoteType {
    UP_VOTE (10),
    DOWN_VOTE (-5);

    private Integer value;

    public Integer getValue(){
        return value;
    }

    VoteType(Integer value) {
        this.value = value;
    }
}