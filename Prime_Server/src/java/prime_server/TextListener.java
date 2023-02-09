/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prime_server;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author sarun
 */
public class TextListener implements MessageListener {
    private MessageProducer replyProducer;
    private Session session;
    
    public TextListener(Session session) {
              
        this.session = session;
        try {
            replyProducer = session.createProducer(null);
        } catch (JMSException ex) {
            Logger.getLogger(TextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;

        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                System.out.println("Reading message : " + msg.getText());
            } else {
                System.err.println("Message is not a TextMessage");
            }
            
            // set up the reply message 
            String data[] = msg.getText().split(",");
            String replyMsg = "";
            if(data.length == 2){
                try{
                    int startNum = Integer.parseInt(data[0]), endNum = Integer.parseInt(data[1]);
                    PrimeCount pc = new PrimeCount();
                    int result = pc.calculatePrime(startNum, endNum);
                    replyMsg = startNum + "," + endNum + "," + result;
                } catch(Exception e){
                    replyMsg = "Invalid data type!!!";
                }
            }
            else{
                replyMsg = "Invalid data format!!!";
            }
            
            TextMessage response = session.createTextMessage(replyMsg); 
            response.setJMSCorrelationID(message.getJMSCorrelationID());
            System.out.println("Sending message : " + response.getText());
            replyProducer.send(message.getJMSReplyTo(), response);
            
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage() : " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage() : " + t.getMessage());
        }
        
    }
}
