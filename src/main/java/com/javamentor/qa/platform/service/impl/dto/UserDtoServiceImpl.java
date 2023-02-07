package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.models.dto.UserProfileReputationDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ali Veliev 29.11.2021
 */

@Service
public class UserDtoServiceImpl extends PageDtoServiceImpl<UserDto> implements UserDtoService {

    private UserDtoDao userDtoDao;
    private TagDtoDao tagDtoDao;

    @Autowired
    public UserDtoServiceImpl(UserDtoDao userDtoDao, TagDtoDao tagDtoDao) {
        this.userDtoDao = userDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Transactional
    public Optional<UserDto> getUserById(Long id) {
        return userDtoDao.getUserById(id).map(dto -> {
            dto.setTopTags(tagDtoDao.getTop3UserTagsByReputation(id));
            return dto;
        });
    }

    @Override
    public List<UserProfileQuestionDto> getAllQuestionsByUserId(Long id) {
        List<UserProfileQuestionDto> listUserProfileQuestionDto = userDtoDao.getAllQuestionsByUserId(id);
        setListTagDtoIntoUserProfileQuestionDto(listUserProfileQuestionDto);

        return listUserProfileQuestionDto;
    }

    @Override
    public List<UserProfileQuestionDto> getAllDeletedQuestionsByUserId(Long id) {
        List<UserProfileQuestionDto> listUserProfileDeletedQuestionDto = userDtoDao.getAllDeletedQuestionsByUserId(id);
        setListTagDtoIntoUserProfileQuestionDto(listUserProfileDeletedQuestionDto);

        return listUserProfileDeletedQuestionDto;
    }

    @Override
    public List<UserProfileReputationDto> getReputationByUserId(Long id) {
        return userDtoDao.getReputationByUserId(id);
    }

    private void setListTagDtoIntoUserProfileQuestionDto(List<UserProfileQuestionDto> listUserProfileQuestionDto){
        List<Long> listIdQuestion = listUserProfileQuestionDto
                .stream()
                .map(UserProfileQuestionDto::getQuestionId)
                .collect(Collectors.toList());
        Map<Long, List<TagDto>> tags = tagDtoDao.getTagsByQuestionIds(listIdQuestion);

        for (UserProfileQuestionDto e : listUserProfileQuestionDto) {
            e.setListTagDto(tags.get(e.getQuestionId()));
        }

    }

    @Transactional
    @Override
    public List<UserDto> getTop10UserDtoForAnswer() {
        return userDtoDao.getTop10UserDtoForAnswer();
    }

    @Transactional
    @Override
    public List<UserDto> getTop10UserDtoForAnswerOnTheMonth() {
        return userDtoDao.getTop10UserDtoForAnswerOnTheMonth();
    }

    @Transactional
    @Override
    public List<UserDto> getTop10UserDtoForAnswerOnTheYear() {
        return userDtoDao.getTop10UserDtoForAnswerOnTheYear();
    }

}
