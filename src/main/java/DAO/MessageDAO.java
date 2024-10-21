package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
   public Message insertMessage (Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL logic
            String sql = "INSERT INTO message (posted_by,message_text,time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2,message.getMessage_text());
            preparedStatement.setLong(3,message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generate_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generate_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //SQL logic
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    //aun no sale
    public Message getMessageById (int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL logic
            String sql = "SELECT * FROM message WHERE message_id = ?;";  

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean deleteMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL logic
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,id);

            int rs = preparedStatement.executeUpdate();
            if (rs>0) {
                return true;    
            }
            else{
                return false;
                }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateMessageText(int id, String messageText){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //SQL logic
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,messageText);
            preparedStatement.setInt(2,id);

            int rs = preparedStatement.executeUpdate();
            System.out.println(rs);
            if (rs>0) {
                return true;    
            }
            else{
                return false;
                }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<Message> getAllMessagesByAccountId(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //SQL logic
            String sql = "SELECT * FROM Message WHERE posted_by = ?;";  

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,account_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

}
