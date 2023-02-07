package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.SingleChatDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository
@NoArgsConstructor
public class PaginationAllSingleChatsOfUser implements PageDtoDao<SingleChatDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SingleChatDto> getItems(Map<String, Object> params) {

        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        return entityManager.createQuery("select new com.javamentor.qa.platform.models.dto.SingleChatDto(" +
                        "singleChat.chat.id, " +
                        "case " +
                            "when singleChat.userOne.id = :userId then singleChat.useTwo.nickname " +
                            "else singleChat.userOne.nickname " +
                        "end, " +
                        "case " +
                            "when singleChat.userOne.id = :userId then singleChat.useTwo.imageLink " +
                            "else singleChat.userOne.imageLink " +
                        "end, " +
                        "message.message, message.persistDate) " +
                        "from SingleChat singleChat " +
                        "join Message message on message.chat.id = singleChat.chat.id " +
                        "where message.persistDate " +
                        "in (" +
                            "select max(messageMaxDate.persistDate) " +
                            "from Message messageMaxDate " +
                            "where " +
                            "messageMaxDate.chat.id = singleChat.chat.id) " +
                        "and (singleChat.userOne.id = :userId " +
                        "or singleChat.useTwo.id = :userId) " +
                        "and " +
                        "case when singleChat.userOne.id = :userId then singleChat.isDeleteOne " +
                        "else singleChat.isDeleteTwo end = false " +
                        "order by message.persistDate desc")
                .setParameter("userId", params.get("userId"))
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {
        final String query =
                "select singleChat from SingleChat singleChat " +
                        "join fetch singleChat.userOne userOne " +
                        "join fetch singleChat.useTwo userTwo " +
                        "where userOne.id = :userId " +
                        "or userTwo.id = :userId";

        return entityManager.createQuery(query).setParameter("userId", params.get("userId"))
                .getResultList().size();
    }
}
