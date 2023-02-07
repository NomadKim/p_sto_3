package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Ali Veliev 10.12.2021
 */

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public Optional<QuestionDto> getQuestionByQuestionIdAndUserId(Long questionId, Long userId) {

        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        "select q.id, " +
                                "q.title, " +
                                "q.description,  " +
                                "q.lastUpdateDateTime,  " +
                                "q.persistDateTime,  " +
                                "u.id,  " +
                                "u.fullName,  " +
                                "u.imageLink, " +
                                "coalesce((select sum(r.count) from Reputation r where r.author.id = u.id), 0), " +
                                "coalesce((select sum(case v.vote  when 'UP_VOTE' then 1 else -1 end) from VoteQuestion v where v.question.id=q.id), 0), " +
                                "(select count(qv.id) from QuestionViewed qv where qv.question.id = q.id), " +
                                "(select count(a.id) from Answer a where a.question.id = q.id), " +
                                "(select quv.vote from VoteQuestion quv where quv.question.id = q.id and quv.user.id =:userId), " +
                                "(select count(b) from BookMarks b where b.question.id = :questionId and b.user.id = :userId), " +
                                "(select (case when count(a) > 0 then true else false end) from Answer a where a.question.id = :questionId and a.user.id = :userId)" +
                                "from Question  q " +
                                "LEFT join q.user u " +
                                "where q.id =:questionId")

                .setParameter("questionId", questionId)
                .setParameter("userId", userId)
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {

                                          @Override
                                          public Object transformTuple(Object[] tuple, String[] aliases) {

                                              QuestionDto questionDto = new QuestionDto();
                                              questionDto.setId(((Long) tuple[0]));
                                              questionDto.setTitle((String) tuple[1]);
                                              questionDto.setDescription((String) tuple[2]);
                                              questionDto.setLastUpdateDateTime(((LocalDateTime) tuple[3]));
                                              questionDto.setPersistDateTime(((LocalDateTime) tuple[4]));
                                              questionDto.setAuthorId(((Long) tuple[5]));
                                              questionDto.setAuthorName((String) tuple[6]);
                                              questionDto.setAuthorImage((String) tuple[7]);
                                              questionDto.setAuthorReputation(((Long) tuple[8]));
                                              questionDto.setCountValuable(((Long) tuple[9]).intValue());
                                              questionDto.setViewCount(((Long) tuple[10]).intValue());
                                              questionDto.setCountAnswer(((Long) tuple[11]).intValue());
                                              questionDto.setIsUserVote((Enum<VoteType>) tuple[12]);
                                              questionDto.setIsUserBookmark(tuple[13].equals(1L));
                                              questionDto.setIsUserAnswerVote((Boolean) tuple[14]);

                                              return questionDto;
                                          }

                                          @Override
                                          public List transformList(List list) {
                                              return list;
                                          }
                                      }
                ));
    }
}