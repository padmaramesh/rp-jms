package padma.ramesh.listner;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import padma.ramesh.config.JmsConfig;
import padma.ramesh.model.HelloWorldMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloMessageListner {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listenSend(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers, Message message){

        System.out.println("I got a ACTIVE_QUEUE message");

        System.out.println(helloWorldMessage);

    }

    @JmsListener(destination = JmsConfig.ACTIVE_QUEUE)
    public void listenSendAndRecieve(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers, Message message) throws JMSException {

        HelloWorldMessage payloadMessage = HelloWorldMessage
                .builder()
                .uuid(UUID.randomUUID())
                .message("World!")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(),payloadMessage);



    }
}
