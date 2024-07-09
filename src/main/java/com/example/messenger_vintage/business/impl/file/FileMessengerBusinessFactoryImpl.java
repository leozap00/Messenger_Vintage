package com.example.messenger_vintage.business.impl.file;


import com.example.messenger_vintage.business.MessageService;
import com.example.messenger_vintage.business.MessengerBusinessFactory;
import com.example.messenger_vintage.business.UserService;

import java.io.File;

public class FileMessengerBusinessFactoryImpl extends MessengerBusinessFactory {

    private UserService userService;
    private MessageService messageService;

    private static final String REPOSITORY_BASE = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "Dati";
    private static final String UTENTI_FILE_NAME = REPOSITORY_BASE + File.separator + "users.txt";
    private static final String MESSAGE_FILE_NAME = REPOSITORY_BASE + File.separator + "messages.txt";

    public FileMessengerBusinessFactoryImpl() {
        userService = new FileUserServiceImpl(UTENTI_FILE_NAME);
        messageService = new FileMessageServiceImpl(MESSAGE_FILE_NAME);

    }

    @Override
    public UserService getUserService() {return userService;}

    @Override
    public MessageService getMessageService() {return messageService;}
}
