package com.example.messenger_vintage.business.impl.file;

import com.example.messenger_vintage.business.*;

import com.example.messenger_vintage.business.UserNotFoundException;
import com.example.messenger_vintage.business.UserService;
import com.example.messenger_vintage.domain.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileUserServiceImpl implements UserService {

    private static String path;

    public FileUserServiceImpl(String path) {
        this.path = path;
    }
    @Override
    public User authenticate(String username, String password) throws BusinessException {
        try{
            FileData fileData = Utility.readAllRows(path);
            for(String[] columns : fileData.getRows()){
                if(columns[1].equals(username) && columns[2].equals(password)){
                    User user = new User();
                    if(user != null) {
                        user.setId(Integer.parseInt(columns[0]));
                        user.setUsername(username);
                        user.setPassword(password);
                        user.setName(columns[4]);
                        user.setSurname(columns[5]);
                        user.setStatus(columns[6]);
                    } else{
                        throw new BusinessException("Errore nella lettura del file");
                    }
                    return user;
                }
            }
            throw new UserNotFoundException("User not found");

        } catch (IOException e){
            e.printStackTrace();
            e.getCause();
            throw new BusinessException(e);

        }
    }

    @Override
    public int buildUser(User user) throws BusinessException {
        try{
            FileData fileData = Utility.readAllRows(path);
            List<String[]> rows = fileData.getRows();

            try(PrintWriter writer = new PrintWriter(path)){
                long counter = fileData.getCounter();
                writer.println((counter+1));
                for(String[] columns : fileData.getRows()){
                    writer.println(String.join(Utility.COLUMN_SEPARATOR, columns));
                }
                writer.println(createUserString(user,rows.size()+1));
            }
            return rows.size()+1;
        }catch (IOException e){
            e.printStackTrace();
            throw new BusinessException(e);

        }
    }

    @Override
    public void updateUser(User user) throws BusinessException {

    }

    @Override
    public void editUser(User user) throws UserNotFoundException {
        try {
            FileData fileData = Utility.readAllRows(path);
            for (String[] columns : fileData.getRows()) {
                if (columns[1].equals(user.getUsername()) && !columns[0].equals(Integer.toString(user.getId()))) {
                    throw new UserNotFoundException();
                }
            }
            try (PrintWriter writer = new PrintWriter(path)) {
                writer.println(fileData.getCounter());
                for (String[] columns : fileData.getRows()) {
                    if (Integer.parseInt(columns[0]) == user.getId()) {
                        writer.println(createUserString(user, user.getId()));
                    } else {
                        writer.println(String.join(Utility.COLUMN_SEPARATOR, columns));

                    }
                }
            }

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void deleteUser(int id) throws BusinessException {

    }

    @Override
    public String findUser(User user) throws BusinessException {
        try{
            FileData fileData = Utility.readAllRows(path);
            for (String[] columns : fileData.getRows()) {
                if(columns[1].equals(user.getUsername()) && columns[2].equals(user.getPassword()))
                    return  "Username e password già esistenti";
                if(columns[4].contentEquals(user.getName()) && columns[5].contentEquals(user.getSurname()))
                    return "Utente già registrato";
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
            throw new BusinessException(e);
        }

    }

    @Override
    public User getUserById(int id) throws UserNotFoundException, BusinessException {
        for(User result : getUsers())
            if(result.getId().equals(id))
                return result;
        return null;
    }

    @Override
    public Set<User> getUsers() throws BusinessException {
        Set<User> result = new HashSet<>();
        try{
            FileData fileData = Utility.readAllRows(path);
            if(fileData.getRows().size()>0) {
                for(String[] column : fileData.getRows()){
                    User user = null;
                    user = new User();
                    if(user != null){
                        user.setId(Integer.parseInt(column[0]));
                        user.setUsername(column[1]);
                        user.setPassword(column[2]);
                        user.setName(column[4]);
                        user.setSurname(column[5]);
                        user.setStatus(column[6]);
                        result.add(user);
                    }
                }
            }

        } catch (IOException e){
            e.printStackTrace();
            throw new BusinessException();
        }
        return result;
    }

    private StringBuilder createUserString(User user, long counter) {
        StringBuilder row = new StringBuilder();
        row.append(counter);
        row.append(Utility.COLUMN_SEPARATOR);
        row.append(user.getUsername());
        row.append(Utility.COLUMN_SEPARATOR);
        row.append(user.getPassword());
        row.append(Utility.COLUMN_SEPARATOR);
        row.append("Utente");
        row.append(Utility.COLUMN_SEPARATOR);
        row.append(user.getName());
        row.append(Utility.COLUMN_SEPARATOR);
        row.append(user.getSurname());
        row.append(Utility.COLUMN_SEPARATOR);
        row.append(user.getStatus());
        return row;
    }
}
