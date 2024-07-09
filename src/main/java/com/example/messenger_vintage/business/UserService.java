package com.example.messenger_vintage.business;

import com.example.messenger_vintage.domain.User;

import java.util.Set;

public interface UserService {

    User authenticate(String username, String password) throws BusinessException;

    int buildUser(User user) throws BusinessException;

    void updateUser(User user) throws BusinessException;

    void editUser(User user) throws BusinessException;

    void deleteUser(int id) throws  BusinessException;

    String findUser(User user) throws BusinessException;

    User getUserById(int id) throws UserNotFoundException, BusinessException;

    Set<User> getUsers() throws BusinessException;
}
