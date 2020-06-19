package padma.ramesh.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import padma.ramesh.config.JmsConfig;
import padma.ramesh.model.HelloWorldMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper;

   /* @Scheduled(fixedRate = 2000)
    public void sendMessage(){
        System.out.println("I am Sending message: ");
        HelloWorldMessage message = HelloWorldMessage.builder().uuid(UUID.randomUUID()).message("Hello World").build();
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE,message);

    }
*/
    @Scheduled(fixedRate = 2000)
    public void sendAndRecieveMessage() throws JMSException {

        System.out.println("I am Sendingand Recieving message: ");

        HelloWorldMessage message = HelloWorldMessage.
                builder()
                .uuid(UUID.randomUUID())
                .message("Hello")
                .build();
        Message recoevedMessage = jmsTemplate.sendAndReceive(JmsConfig.ACTIVE_QUEUE, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message helloMessage = null;
                try {
                    helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type","padma.ramesh.model.HelloWorldMessage");

                    System.out.println("Sending hello!");
                    return helloMessage;
                } catch (JsonProcessingException e) {
                    throw new JMSException("boom");
                }
            }
        });

        System.out.println(recoevedMessage.getBody(String.class));
    }
}
