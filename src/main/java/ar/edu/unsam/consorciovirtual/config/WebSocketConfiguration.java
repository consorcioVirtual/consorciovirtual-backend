package ar.edu.unsam.consorciovirtual.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private static final String CHAT_ENDPOINT = "/chat";
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(getChatWebSockethandler(), CHAT_ENDPOINT).setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler getChatWebSockethandler() {
        return new ChatWebSocketHandler();
    }
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry){
//        registry.addEndpoint("/ws-chat");
//    };
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry){
//        //Endpoint para la salida de mensajes
//        registry.enableSimpleBroker("/topic");
//        //Endpoint para la entrada de mensajes
//        registry.setApplicationDestinationPrefixes("/chat");

}
