package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getIgnoredTags(Long userId) {
        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.TagDto(it.id, it.ignoredTag.name, it.ignoredTag.description) " +
                                "from IgnoredTag it where it.user.id = :userId", TagDto.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<TagDto> getTagsByQuestionId(Long id) {
        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto." +
                "TagDto(tag.id, tag.name, tag.description) " +
                "from Tag tag " +
                "left join tag.questions as questions " +
                "where questions.id=: id " +
                "order by tag.id", TagDto.class).setParameter("id", id).getResultList();
    }

    @Override
    public Map<Long, List<TagDto>> getTagsByQuestionIds(List<?> ids) {

        List<Map<Long, List<TagDto>>> list = entityManager.createQuery(
                        "select q.id, " +
                                "t.id, " +
                                "t.name, " +
                                "t.description " +
                                "from Question q " +
                                "join q.tags t " +
                                "where q.id in :ids ")
                .setParameter("ids", ids)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    Map<Long, List<TagDto>> map = new HashMap<>();

                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {

                        List<TagDto> tagDtoList = map.computeIfAbsent(
                                (Long) objects[0],
                                id -> new ArrayList<>());
                        tagDtoList.add(new TagDto(
                                (Long) objects[1],
                                (String) objects[2],
                                (String) objects[3]
                        ));
                        return map;
                    }

                    @Override
                    public List<Map<Long, List<TagDto>>> transformList(List list) {
                        List<Map<Long, List<TagDto>>> resultList = new ArrayList<>();
                        resultList.add(map);
                        return resultList;
                    }
                })
                .getResultList();

        return list.get(0);
    }

    @Override
    public List<TagDto> getTop10FoundTags(String searchString) {
        return entityManager.createQuery("SELECT new com.javamentor.qa.platform.models.dto.TagDto (t.id, t.name, t.description) " +
                        "FROM Tag t " +
                        "WHERE t.name LIKE :searchString " +
                        "ORDER BY t.questions.size DESC", TagDto.class)
                .setParameter("searchString", "%" + searchString + "%")
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public List<TagDto> getTop3UserTagsByReputation(Long userId) {
        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.TagDto(" +
                                "t.id, " +
                                "t.name, " +
                                "t.description) " +
                                "from Reputation reputation " +
                                "join reputation.answer reputation_answer " +
                                "join reputation_answer.question reputation_answer_question " +
                                "join reputation_answer_question.tags t " +
                                "join reputation_answer.voteAnswers vote_answers " +
                                "where reputation.author.id =: id " +
                                "and vote_answers.vote = 'UP_VOTE' " +
                                "group by t.id " +
                                "order by count(vote_answers.id) desc"
                        , TagDto.class)
                .setParameter("id", userId)
                .setMaxResults(3)
                .getResultList();
    }
}