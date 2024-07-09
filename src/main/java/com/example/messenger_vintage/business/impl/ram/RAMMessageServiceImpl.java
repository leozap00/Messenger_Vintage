package com.example.messenger_vintage.business.impl.ram;

import com.example.messenger_vintage.business.BusinessException;
import com.example.messenger_vintage.business.MessageService;
import com.example.messenger_vintage.domain.Message;
import com.example.messenger_vintage.domain.User;

import java.util.*;

public class RAMMessageServiceImpl implements MessageService {

    private List<Message> messages = new ArrayList<>();

    public RAMMessageServiceImpl() {
        Message message = new Message();
        message.setId(1);
        messages.add(message);
    }

    @Override
    public void sendMessage(Message message)  {
        message.setId(messages.size() + 1);
        messages.add(message);

    }

    @Override
    public void deleteMessage(long id) throws BusinessException {

    }

    @Override
    public Set<Message> getMessagesByUser(User user)  {
        Set<Message> result = new HashSet<>();

        for (Message msg : messages) {
            if (msg.getSender().equals(user)) {
                result.add(msg);
            }
        }
        return result;
    }

    @Override
    public List<Message> getMessagesBetweenUsers(User user, User otherUser) throws BusinessException {
        return List.of();
    }
}
