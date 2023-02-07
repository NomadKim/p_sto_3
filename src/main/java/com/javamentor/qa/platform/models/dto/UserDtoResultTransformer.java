package com.javamentor.qa.platform.models.dto;

import org.hibernate.transform.ResultTransformer;

import java.util.List;

public class UserDtoResultTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] tuples, String[] aliases) {
        UserDto userDto = new UserDto();
        userDto.setId((Long)tuples[0]);
        userDto.setEmail((String)tuples[1]);
        userDto.setFullName((String)tuples[2]);
        userDto.setLinkImage((String)tuples[3]);
        userDto.setCity((String)tuples[4]);
        userDto.setReputation((int)tuples[5]);
        return userDto;
    }

    @Override
    public List transformList(List list) {
        return list;
    }
}
