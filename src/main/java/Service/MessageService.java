package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }    

    public Message addMessage(Message message){
        String messageText = message.getMessage_text();
        if(messageText.length()<=255&&messageText.length()>0){
            return messageDAO.insertMessage(message);
        }    
        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        Message messageTemp = new Message();
        messageTemp =  messageDAO.getMessageById(id);
        return messageTemp;
    }

    public Message deleteMessageById(int id){
        boolean deletedMessageFlag;
        Message message = messageDAO.getMessageById(id);
        if (message!=null){
            deletedMessageFlag = messageDAO.deleteMessageById(id);
            if (deletedMessageFlag){
                return message;
            }    
        }
        return null;
    }

    public Message updateMessageText(int id, String messageText){
        boolean updatedMessageFlag;
        Message message = new Message();
        if(messageText.length()<=255 && messageText.length()>0){
            updatedMessageFlag = messageDAO.updateMessageText(id,messageText);
            if (updatedMessageFlag){
                message = messageDAO.getMessageById(id);
                return message;
            }
        }    
        return null;
    }    

    public List<Message> getAllMessagesByAccountId(int account_id) {
        return messageDAO.getAllMessagesByAccountId(account_id);
    }

}
