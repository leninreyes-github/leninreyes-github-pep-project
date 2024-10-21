package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    public SocialMediaController(){
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postRegisterHandler);//1: Our API should be able to process new User registrations.
        app.post("/login", this::postLoginHandler); //2: Our API should be able to process User logins.
        app.post("/messages", this::postMessagesHandler);//3: Our API should be able to process the creation of new messages.
        app.get("/messages", this::getAllMessagesHandler);//4: Our API should be able to retrieve all messages.
        app.get("/messages/{message_id}",this::getMessageByIdHandler);//5: Our API should be able to retrieve a message by its ID.
        app.delete("/messages/{message_id}",this::removeMessageByIdHandler);//6: Our API should be able to delete a message identified by a message ID.
        app.patch("/messages/{message_id}", this::updateMessageTxtHandler);//7: Our API should be able to update a message text identified by a message ID.
        app.get("/accounts/{account_id}/messages",this::getAllMessagesByAccountIdHandler);//8: Our API should be able to retrieve all messages written by a particular user.
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    /*
    private void exampleHandler(Context context) {
        context.json("sample text");
    }*/

    //OK
    private void postRegisterHandler(Context context) throws JsonMappingException, JsonProcessingException {
        //retrieve the json string from the request body
        String jsonString = context.body();

        //utilize jackson to convert the json string to a user object
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(jsonString, Account.class);
        AccountService accountService = new AccountService();
        account = accountService.addAccount(account);

        if (account!=null){
            //Success
            context.status(200);
            context.json(om.writeValueAsString(account));
        }
        else{
            //Fail
            context.status(400);
        }
    }

    //Ok
    private void postLoginHandler(Context context) throws JsonMappingException, JsonProcessingException{
        //retrieve the json string from the request body
        String jsonString = context.body();

        //utilize jackson to convert the json string to a user object
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(jsonString, Account.class);
        AccountService accountService = new AccountService();
        account = accountService.verifyAccount(account);

        if (account!=null){
            //Success
            context.status(200);
            context.json(om.writeValueAsString(account));
        }
        else{
            //Fail
            context.status(401);
        }

    }

    //created by LReyes
    private void postMessagesHandler(Context context) throws JsonMappingException, JsonProcessingException{
        //retrieve the json string from the request body
        String jsonString = context.body();

        //utilize jackson to convert the json string to a user object
        ObjectMapper om = new ObjectMapper();
        Account account = new Account();
        Message message = om.readValue(jsonString, Message.class);
        AccountService accountService = new AccountService();
        account = accountService.verifyAccountById(message.getPosted_by());
        if (account!=null){
            //Account found
            MessageService messageService = new MessageService();
            message = messageService.addMessage(message);
            if (message!=null){
                //Success
                context.status(200);
                context.json(om.writeValueAsString(message));
            }
            else{
                //Fail
                context.status(400);
            }
        }
        else{
            //Account not found
            context.status(400);
        }
        
    }

    //created by LReyes
    private void getAllMessagesHandler(Context context){
        context.json(messageService.getAllMessages());
        context.status(200);
    }

    //created by LReyes
    private void getMessageByIdHandler(Context context) throws JsonProcessingException{
    
        ObjectMapper om = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));  
        MessageService messageService = new MessageService();
        Message message = messageService.getMessageById(message_id);
        context.status(200);
        if(message==null){
            context.json("");
        }
        else{
            context.json(om.writeValueAsString(message));
        }
    }

    //created by LReyes
    private void removeMessageByIdHandler(Context context) throws JsonProcessingException{
    
        ObjectMapper om = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));  
        MessageService messageService = new MessageService();
        Message removedMessage = messageService.deleteMessageById(message_id);
        if(removedMessage==null){
            context.json("");
        }
        else{
            context.json(om.writeValueAsString(removedMessage));
        }
        context.status(200);

    }

    //created by LReyes
    private void updateMessageTxtHandler(Context context) throws JsonMappingException, JsonProcessingException  {
        String jsonString = context.body();

        //utilize jackson to convert the json string to a user object
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(jsonString, Message.class);
        String messageText = message.getMessage_text();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        MessageService messageService = new MessageService();
        Message updatedMessage = new Message();
        updatedMessage = messageService.updateMessageText(message_id, messageText);
        if(updatedMessage == null){
            context.status(400);
        }else{
            context.json(om.writeValueAsString(updatedMessage));
            context.status(200);
        }

    }

    //created by LReyes
    private void getAllMessagesByAccountIdHandler(Context context){
 
        //ObjectMapper om = new ObjectMapper();
        int account_id = Integer.parseInt(context.pathParam("account_id"));  
        MessageService messageService = new MessageService();
        List<Message> messagesByAccountId = messageService.getAllMessagesByAccountId(account_id);
        if (messagesByAccountId!=null){
            context.json(messagesByAccountId);
        }
        else{
            context.json("");
        }
        
        context.status(200);
    }
 

}