package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserProfileQuestionDto;
import com.javamentor.qa.platform.models.dto.UserProfileReputationDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Ali Veliev 29.11.2021
 */

@Repository
public class UserDtoDaoImpl implements UserDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserDto> getUserById(Long id) {

        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserDto (" +
                                "user.id, " +
                                "user.email, " +
                                "user.fullName, " +
                                "user.imageLink, " +
                                "user.city, " +
                                "(select coalesce (max(rep.count),0) from Reputation rep where rep.author.id =: id) " +
                                ") " +
                                "from User user where user.id =: id "
                        , UserDto.class)
                .setParameter("id", id)
                .getResultStream()
                .findAny();
    }

    @Override
    public List<UserProfileQuestionDto> getAllQuestionsByUserId(Long id) {
        return entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto(" +
                        "q.id, " +
                        "q.title, " +
                        "(select (count(ans.id)) from Answer as ans where ans.question.id = q.id), " +
                        "q.persistDateTime)" +
                        "from Question q where q.user.id = :id", UserProfileQuestionDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<UserProfileQuestionDto> getAllDeletedQuestionsByUserId(Long id) {
        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserProfileQuestionDto(" +
                                "q.id, " +
                                "q.title, " +
                                "(select (count(ans.id)) from Answer as ans where ans.question.id = q.id), " +
                                "q.persistDateTime)" +
                                "from Question q where q.user.id = :id and q.isDeleted = true", UserProfileQuestionDto.class)
                .setParameter("id", id)
                .getResultList();
    }


    @Override
    public List<UserDto> getTop10UserDtoForAnswer() {
        String queryH = "select u.id, u.email, u.fullName, u.imageLink, u.city," +
                "CAST((SELECT COALESCE(SUM(r.count),0) FROM Reputation as r WHERE r.author.id = u.id)as int) as r0, " +
                "(SELECT count(a.id) from Answer as a where a.persistDateTime > :date and a.user.id = u.id) AS r1, " +
                "((SELECT count(va.user.id) from VoteAnswer as va where va.user.id = u.id and va.vote = 'UP_VOTE') - " +
                " (SELECT count(va.user.id) from VoteAnswer as va where va.user.id = u.id and va.vote = 'DOWN_VOTE')) as r2 " +
                "        FROM User as u " +
                "        WHERE u.isEnabled = true " +
                "        ORDER BY r1 desc, r2 desc";

        return entityManager.createQuery(queryH)
                .setParameter("date", LocalDateTime.of(LocalDate.now(), LocalTime.now()).minusWeeks(1))
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {
                        return new UserDto(
                                ((Number) (objects[0])).longValue(),
                                (String) (objects[1]),
                                (String) (objects[2]),
                                (String) (objects[3]),
                                (String) (objects[4]),
                                ((Number) (objects[5])).intValue()
                        );
                    }

                    @Override
                    public List<UserDto> transformList(List list) {
                        return list;
                    }
                })
                .setMaxResults(10).getResultList();
    }

    @Override
    public List<UserDto> getTop10UserDtoForAnswerOnTheMonth() {
        String queryH = "select u.id, u.email, u.fullName, u.imageLink, u.city," +
                "CAST((SELECT COALESCE(SUM(r.count),0) FROM Reputation as r WHERE r.author.id = u.id)as int) as r0, " +
                "(SELECT count(a.id) from Answer as a where a.persistDateTime > :date and a.user.id = u.id) AS r1, " +
                "((SELECT count(va.user.id) from VoteAnswer as va where va.user.id = u.id and va.vote = 'UP_VOTE') - " +
                " (SELECT count(va.user.id) from VoteAnswer as va where va.user.id = u.id and va.vote = 'DOWN_VOTE')) as r2 " +
                "        FROM User as u " +
                "        WHERE u.isEnabled = true " +
                "        ORDER BY r1 desc, r2 desc";

        return entityManager.createQuery(queryH)
                .setParameter("date", LocalDateTime.of(LocalDate.now(), LocalTime.now()).minusMonths(1))
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {
                        return new UserDto(
                                ((Number) (objects[0])).longValue(),
                                (String) (objects[1]),
                                (String) (objects[2]),
                                (String) (objects[3]),
                                (String) (objects[4]),
                                ((Number) (objects[5])).intValue()
                        );
                    }

                    @Override
                    public List<UserDto> transformList(List list) {
                        return list;
                    }
                })
                .setMaxResults(10).getResultList();
    }

    @Override
    public List<UserDto> getTop10UserDtoForAnswerOnTheYear() {
        String queryH = "select u.id, u.email, u.fullName, u.imageLink, u.city," +
                "CAST((SELECT COALESCE(SUM(r.count),0) FROM Reputation as r WHERE r.author.id = u.id)as int) as r0, " +
                "(SELECT count(a.id) from Answer as a where a.persistDateTime > :date and a.user.id = u.id) AS r1, " +
                "((SELECT count(va.user.id) from VoteAnswer as va where va.user.id = u.id and va.vote = 'UP_VOTE') - " +
                " (SELECT count(va.user.id) from VoteAnswer as va where va.user.id = u.id and va.vote = 'DOWN_VOTE')) as r2 " +
                "        FROM User as u " +
                "        WHERE u.isEnabled = true " +
                "        ORDER BY r1 desc, r2 desc";

        return entityManager.createQuery(queryH)
                .setParameter("date", LocalDateTime.of(LocalDate.now(), LocalTime.now()).minusYears(1))
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] objects, String[] strings) {
                        return new UserDto(
                                ((Number) (objects[0])).longValue(),
                                (String) (objects[1]),
                                (String) (objects[2]),
                                (String) (objects[3]),
                                (String) (objects[4]),
                                ((Number) (objects[5])).intValue()
                        );
                    }

                    @Override
                    public List<UserDto> transformList(List list) {
                        return list;
                    }
                })
                .setMaxResults(10).getResultList();
    }


    @Override
    public List<UserProfileReputationDto> getReputationByUserId(Long id) {

        return entityManager.createQuery(
                        "select new com.javamentor.qa.platform.models.dto.UserProfileReputationDto (" +
                                "r.count, " +
                                "r.question.id, " +
                                "r.question.title, " +
                                "r.persistDate) " +
                                "from Reputation r where r.author.id =: id"
                        , UserProfileReputationDto.class)
                .setParameter("id", id)
                .getResultList();
    }
}
