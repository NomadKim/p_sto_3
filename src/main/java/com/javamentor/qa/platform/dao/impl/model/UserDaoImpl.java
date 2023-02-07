package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User, Long> implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Cacheable(value = "getUserByEmail", key = "#email")
    public Optional<User> getUserByEmail(String email) {
        String hql = "select u from User u " +
                "join fetch u.role where u.email = :email";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class).setParameter("email", email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    public List<User> getUsersByIds(List<Long> ids) {
        String hql = "select u from User u where u.id in :ids";
        return entityManager.createQuery(hql).setParameter("ids", ids).getResultList();
    }


    @Cacheable(value = "checkIfExists", key = "#email")
    public boolean checkIfExists(String email) {
        return (boolean) entityManager.createQuery(" SELECT COUNT(e) > 0 FROM User e"
                + "  WHERE e.email =: email").setParameter("email", email).getSingleResult();
    }

    @CacheEvict(value = {"getUserByEmail", "checkIfExists"}, key = "#email")
    public void updatePasswordByEmail(String email, String password) {
        String hql = "update User u set u.password = :password where u.email = :email";
        entityManager.createQuery(hql)
                .setParameter("password", password)
                .setParameter("email", email).executeUpdate();
    }

    @Override
    @CacheEvict(value = {"getUserByEmail", "checkIfExists"}, key = "#email")
    public void disableUserByEmail(String email) {
        String hql = "update User u set u.isEnabled = false where u.email = :email";
        entityManager.createQuery(hql).setParameter("email", email).executeUpdate();

    }

    @Override
    @Cacheable(value = "getAllByRole", key = "#role")
    public List<User> getAllByRole(Role role) {
         return entityManager.createQuery("select u from User u join fetch u.role where u.role = :role").setParameter("role", role).getResultList();
    }
}

