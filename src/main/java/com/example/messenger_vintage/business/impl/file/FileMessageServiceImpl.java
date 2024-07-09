package com.example.messenger_vintage.business.impl.file;

import com.example.messenger_vintage.business.BusinessException;
import com.example.messenger_vintage.business.MessageService;
import com.example.messenger_vintage.domain.Message;
import com.example.messenger_vintage.domain.User;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileMessageServiceImpl implements MessageService {

    private static String path;
    private StringBuilder row;

    public FileMessageServiceImpl(String path) { this.path = path;}
    @Override
    public void sendMessage(Message message) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(path);
            List<String[]> rows = fileData.getRows();
            long counter = fileData.getCounter();

            // Aggiunge il nuovo messaggio alla fine dei dati esistenti
            try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {
                message.setId(counter + 1); // Assegna un ID univoco al messaggio
                writer.println(createMessageString(message, message.getId()));
            }

        } catch (IOException e) {
            throw new BusinessException("Error sending message: " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteMessage(long id) throws BusinessException {
        try{
            FileData fileData = Utility.readAllRows(path);
            try(PrintWriter writer = new PrintWriter(path)) {
                writer.println(fileData.getCounter()-1);
                for(String[] column : fileData.getRows()) {
                    if(Integer.parseInt(column[0])==id) {
                        writer.print("");
                    }
                    else {
                        writer.println(String.join(Utility.COLUMN_SEPARATOR,column));
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }


    }

    @Override
    public Set<Message> getMessagesByUser(User user) throws BusinessException {
        Set<Message> result = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(Utility.COLUMN_SEPARATOR);
                if (columns.length >= 5 && columns[3].equals(String.valueOf(user.getId()))) {
                    Message message = new Message();
                    message.setId(Long.parseLong(columns[0]));
                    message.setSender(user);
                    message.setText(columns[1]);
                    message.setTimeStamp(LocalDateTime.parse(columns[4]));
                    result.add(message);
                }
            }
        } catch (IOException e) {
            throw new BusinessException("Error getting messages: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Message> getMessagesBetweenUsers(User user, User otherUser) throws BusinessException {
        List<Message> result = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while((line = reader.readLine())!= null) {
                String[] columns = line.split(Utility.COLUMN_SEPARATOR);

                if(columns.length >= 5 ) {
                    try{
                        long messageId = Long.parseLong(columns[0]);
                        String text = columns[1];
                        long senderId = Long.parseLong(columns[2]);
                        long recipientId = Long.parseLong(columns[3]);
                        LocalDateTime timestamp = LocalDateTime.parse(columns[4]);


                        if((senderId == user.getId() && recipientId == otherUser.getId()) ||
                                (senderId == otherUser.getId() && recipientId == user.getId())) {
                            Message message = new Message();
                            message.setId(messageId);
                            message.setText(text);
                            message.setTimeStamp(timestamp);

                            if(senderId == user.getId()) {
                                message.setSender(user);
                                message.setRecipient(otherUser);
                            } else{
                                message.setSender(otherUser);
                                message.setRecipient(user);
                            }

                            result.add(message);
                        }
                    } catch (NumberFormatException | DateTimeParseException e) {
                        // Ignora le righe mal formate e stampa un messaggio di errore
                        System.err.println("Riga mal formata: " + line + " - " + e.getMessage());
                    }

                }

            }
        } catch (IOException e) {
            throw new BusinessException("Error getting messages: " + e.getMessage(), e);
        }
        result.sort(Comparator.comparing(Message::getTimeStamp));
        return result;
    }

    private String createMessageString(Message message, long counter) {
        StringBuilder row = new StringBuilder();
        row.append(counter);
        row.append(Utility.COLUMN_SEPARATOR);
        row.append(message.getText()); // Testo del messaggio
        row.append(Utility.COLUMN_SEPARATOR);
        row.append(message.getSender().getId()); // ID del mittente
        row.append(Utility.COLUMN_SEPARATOR);
        row.append(message.getRecipient().getId()); // ID del destinatario
        row.append(Utility.COLUMN_SEPARATOR);
        row.append(message.getTimeStamp()); // Timestamp del messaggio
        return row.toString();
    }
}
