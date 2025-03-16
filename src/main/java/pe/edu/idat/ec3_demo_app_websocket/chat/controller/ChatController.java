package pe.edu.idat.ec3_demo_app_websocket.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import pe.edu.idat.ec3_demo_app_websocket.chat.model.Mensaje;




//metodo se encarga de recibir el mensaje y mandarlo al websocket
@Controller
public class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Mensaje enviarMensaje(@Payload Mensaje mensaje){
        return mensaje;
    }


    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Mensaje agregarUsuario(Mensaje mensaje, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username",mensaje.getEnvio());
        return mensaje;

    }
}

