package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.models.dto.CommentDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CommentDtoDaoImpl implements CommentDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CommentDto> getCommentDtosByQuestionId(Long id) {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
                                "(comment.id," +
                                "comment.text, " +
                                "comment.user.id, " +
                                "comment.user.fullName, " +
                                "(SELECT SUM (r.count) FROM Reputation r WHERE r.author.id = comment.user.id), " +
                                "comment.persistDateTime)" +
                                "FROM Comment comment " +
                                "left JOIN CommentQuestion commentQuestion ON (comment.id = commentQuestion.comment.id) " +
                                "WHERE commentQuestion.question.id = :id " +
                                "order by comment.persistDateTime desc"
                        , CommentDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Optional<CommentDto> getCommentDtoByCommentId(Long id) {
        return entityManager.createQuery(
                        "SELECT new com.javamentor.qa.platform.models.dto.CommentDto" +
                                "(comment.id," +
                                "comment.text, " +
                                "comment.user.id, " +
                                "comment.user.fullName, " +
                                "(SELECT (sum(r.count)) FROM Reputation r WHERE r.author.id = comment.user.id), " +
                                "comment.persistDateTime)" +
                                "FROM Comment comment WHERE comment.id = :id ", CommentDto.class)
                .setParameter("id", id)
                .getResultStream()
                .findAny();
    }

    @Override
    public Map<Long, List<CommentDto>> getCommentDtosByAnswerIds(List<Long> ids) {
        final String  queryCommentDto = "select comment.id, " +
                "comment.text, comment.user.id, " +
                "comment.user.fullName, " +
                "(SELECT SUM (r.count) FROM Reputation r WHERE r.author.id = comment.user.id), " +
                "comment.persistDateTime, commentAnswer.answer.id " +
                "from Comment comment " +
                "inner join CommentAnswer commentAnswer " +
                "on comment.id = commentAnswer.comment.id " +
                "where commentAnswer.answer.id in :ids order by comment.persistDateTime desc";

        List<Map<Long, List<CommentDto>>> listMapCommentDto =  entityManager.createQuery(queryCommentDto).setParameter("ids", ids)
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {
                    Map<Long, List<CommentDto>> map = new HashMap<>();

                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {
                        List<CommentDto> commentDtoList = map.computeIfAbsent(
                                (Long) objects[6],
                                id -> new ArrayList<>());
                        commentDtoList.add(new CommentDto((Long) objects[0],
                                (String) objects[1],
                                (Long) objects[2],
                                (String) objects[3],
                                (Long) objects[4],
                                (LocalDateTime) objects[5]));
                        return map;
                    }

                    @Override
                    public List<Map<Long, List<CommentDto>>> transformList(List list) {
                        List<Map<Long, List<CommentDto>>> resultList = new ArrayList<>();
                        resultList.add(map);
                        return resultList;
                    }
                })
                .getResultList();
        return listMapCommentDto.get(0);
    }
}
