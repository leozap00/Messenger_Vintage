package com.example.messenger_vintage.business;


import com.example.messenger_vintage.business.impl.dao.DAOMessengerBusinessFactoryImpl;
import com.example.messenger_vintage.business.impl.file.FileMessengerBusinessFactoryImpl;

public abstract class MessengerBusinessFactory {

    //private static MessengerBusinessFactory factory = new FileMessengerBusinessFactoryImpl();

    private static MessengerBusinessFactory factory = new DAOMessengerBusinessFactoryImpl();

    public static MessengerBusinessFactory getInstance() {return factory; }

    public abstract UserService getUserService();

    public abstract MessageService getMessageService();

}
