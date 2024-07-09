package com.example.messenger_vintage.business;

import com.example.messenger_vintage.domain.Message;
import com.example.messenger_vintage.domain.User;


import java.util.List;
import java.util.Set;

public interface MessageService {

    void sendMessage(Message message) throws BusinessException;

    void deleteMessage(long id) throws BusinessException;

    Set<Message> getMessagesByUser(User user) throws BusinessException;

    List<Message> getMessagesBetweenUsers(User user, User otherUser) throws BusinessException;
}
