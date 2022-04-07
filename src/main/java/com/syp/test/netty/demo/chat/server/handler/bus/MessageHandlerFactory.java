package com.syp.test.netty.demo.chat.server.handler.bus;

import com.google.common.collect.Maps;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

/**
 * @Author shiyuping
 * @Date 2022/4/7 10:47
 */
public class MessageHandlerFactory {

    private static final Map<Integer, Object> messageHandlerMap = Maps.newConcurrentMap();

    public static AbstractMessageHandler getMessageHandler(int messageType){

        return (AbstractMessageHandler)messageHandlerMap.get(messageType);

        /*AbstractMessageHandler messageHandler = null;
        switch (messageType){
            case Message.ChatRequestMessage:
                messageHandler = new ChatRequestMessageHandler();
                break;
            case Message.GroupChatRequestMessage:
                messageHandler = new GroupChatRequestMessageHandler();
                break;
            case Message.GroupCreateRequestMessage:
                messageHandler =  new GroupCreateRequestMessageHandler();
                break;
            case Message.GroupJoinRequestMessage:
                messageHandler =  new GroupJoinRequestMessageHandler();
                break;
            case Message.GroupMembersRequestMessage:
                messageHandler =  new GroupMembersRequestMessageHandler();
                break;
            case Message.GroupQuitRequestMessage:
                messageHandler =  new GroupQuitRequestMessageHandler();
                break;
            case Message.LoginRequestMessage:
                messageHandler =  new LoginRequestMessageHandler();
                break;
            default:
        }
        return messageHandler;*/
    }

    static {
        Reflections reflections = new Reflections(MessageHandlerFactory.class.getPackage().getName());
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(MessageType.class);
        for (Class<?> auditModeClass : classSet) {
            MessageType annotation = auditModeClass.getAnnotation(MessageType.class);
            try {
                messageHandlerMap.put(annotation.value(), auditModeClass.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
