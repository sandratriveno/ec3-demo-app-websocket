package pe.edu.idat.ec3_demo_app_websocket.config;

import jakarta.websocket.Session;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import pe.edu.idat.ec3_demo_app_websocket.chat.model.Mensaje;
import pe.edu.idat.ec3_demo_app_websocket.chat.model.TipoMensaje;

@Component
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageSendingOperations;

    public WebSocketEventListener(SimpMessageSendingOperations messageSendingOperations) {
        this.messageSendingOperations = messageSendingOperations;
    }

    @EventListener
    public void socketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String usuario = (String)headerAccessor.getSessionAttributes().get("username");

        if( usuario!=null){
            Mensaje mensaje=new Mensaje();
            mensaje.setTipo(TipoMensaje.DEJAR);
            mensaje.setEnvio(usuario);
            messageSendingOperations.convertAndSend("/topic/public",mensaje);

        }
    }
}
