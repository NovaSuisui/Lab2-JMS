/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prime_server;

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
        MessageProducer replyProducer = null;
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        TextMessage message = null;
        TextListener listener = null;
        
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(queue);
            listener = new TextListener(session);
            consumer.setMessageListener(listener);
            connection.start();
            String ch = "";
            Scanner in = new Scanner(System.in);
            while(true) {
                System.out.println("Server is Ready");
                System.out.print("Press q to quit ");
                ch = in.nextLine();
                if (ch.equals("q")) {
                    break;
                }
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