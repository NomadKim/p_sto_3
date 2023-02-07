package com.javamentor.qa.platform.models.entity.chat;

import com.javamentor.qa.platform.exception.ApiRequestException;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_chat")
public class GroupChat{

    @Id
    private Long id;

    @Column(name = "is_global")
    private boolean isGlobal = false;

    @Column(name = "title")
    private String title;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    private Chat chat = new Chat(ChatType.GROUP);

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "groupchat_has_users",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    @Column(name = "image_chat")
    private String imageChat;

    @PrePersist
    private void prePersistFunction() {
        checkConstraints();
    }

    @PreUpdate
    private void preUpdateFunction() {
        checkConstraints();
    }

    private void checkConstraints() {
        if (this.chat.getChatType() != ChatType.GROUP) {
            throw new ApiRequestException("У экземпляра Chat, связанного с GroupChat, " +
                    "поле chatType должно принимать значение ChatType.GROUP");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupChat groupChat = (GroupChat) o;
        return Objects.equals(id, groupChat.id) &&
                Objects.equals(chat, groupChat.chat) &&
                Objects.equals(users, groupChat.users) &&
                Objects.equals(title, groupChat.title) &&
                Objects.equals(imageChat, groupChat.imageChat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat, users, imageChat);
    }

}
