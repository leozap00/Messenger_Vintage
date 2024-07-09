package com.example.messenger_vintage.business.impl.ram;


import com.example.messenger_vintage.business.MessageService;
import com.example.messenger_vintage.business.MessengerBusinessFactory;
import com.example.messenger_vintage.business.UserService;

public class RAMMessengerBusinessFactoryImpl extends MessengerBusinessFactory {

    private UserService userService;
    private MessageService messageService;

    public RAMMessengerBusinessFactoryImpl() {

        userService = new RAMUserServiceImpl();
        messageService = new RAMMessageServiceImpl();

    }

    @Override
    public UserService getUserService() {return userService;}

    @Override
    public MessageService getMessageService() {return messageService;}
}
