/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prime_asyncclient;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author SK
 */
public class Main {
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/TempQueue")
    private static Queue queue;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection connection = null;
        TextListener listener = null;
        
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            listener = new TextListener();
            Queue replyDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(replyDest);
            responseConsumer.setMessageListener(listener);
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage();
            message.setJMSReplyTo(replyDest);
            String correlationId = "12345";
            message.setJMSCorrelationID(correlationId);
            connection.start();
            
            String msg = "";
            Scanner in = new Scanner(System.in);
            while(true) {
                System.out.println("Enter two number. Use \",\" to seperate each number. To end the program press 'q'");
                msg = in.nextLine();
                if (msg.equals("q")) {
                    break;
                }
                message.setText(msg);
                System.out.println("Sending message : " + message.getText());
                producer.send(message);
            }
            
        } catch (JMSException e) {
            System.err.println("Exception occurred : " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
}