/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prime_asyncclient;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author sarun
 */
public class TextListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;

        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                String result[] = msg.getText().split(",");
                if(result.length == 3){
                    System.out.println("The number of primes between " + result[0] + " and " + result[1] + " : " + result[2]);
                }
                else{
                    System.out.println(result[0]);
                }
            } else {
                System.err.println("Message is not a TextMessage");
            }
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage() : " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage() : " + t.getMessage());
        }
    }
    
}
