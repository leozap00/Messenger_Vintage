package com.example.messenger_vintage.business.impl.ram;

import com.example.messenger_vintage.business.BusinessException;
import com.example.messenger_vintage.business.UserNotFoundException;
import com.example.messenger_vintage.business.UserService;
import com.example.messenger_vintage.domain.User;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RAMUserServiceImpl implements UserService {

    private Set<User> users = new HashSet<User>();

    @Override
    public User authenticate(String username, String password) throws UserNotFoundException {
        if("Utente".equalsIgnoreCase(username) && "Utente".equalsIgnoreCase(password) ){
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setStatus("Online");
            return user;
        }
        throw new UserNotFoundException();
    }

    @Override
    public int buildUser(User user) { return 0; }

    @Override
    public void updateUser(User user){
        Iterator<User> iter = users.iterator();
        while(iter.hasNext()){
            if(iter.next().getUsername().equals(user.getUsername())){
                users.remove(iter.next());
                users.add(user);
            }
        }
    }

    @Override
    public void editUser(User user) {
        Iterator<User> iter = users.iterator();
        while (iter.hasNext()) {
            User currentUser = iter.next();
            if (currentUser.getId().equals(user.getId())) {
                iter.remove();
                users.add(user);
                return;
            }
        }
        users.add(user); // Aggiunge l'utente se non trovato
    }

    @Override
    public void deleteUser(int id) {}

    @Override
    public String findUser(User user) {
        if(user.getUsername().equals("utente") && user.getPassword().equalsIgnoreCase("Utente"))
            return "Username e password già esistenti";
        return "";
    }

    @Override
    public User getUserById(int id) throws UserNotFoundException { return null; }

    @Override
    public Set<User> getUsers() throws BusinessException{return null;}
}
