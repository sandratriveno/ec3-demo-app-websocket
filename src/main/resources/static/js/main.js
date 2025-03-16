$(document).ready(function(){
    var usernamePage = $("#username-page");
    var chatPage = $("#chat-page");
    var usernameForm = $("#usernameForm");
    var messageForm = $("#messageForm");
    var messageArea = $("#messageArea");
    var connectingElement = $(".connecting");
    var stompClient = null;
    var username = null;

    function conectarUsuario(event){
        username = $("#name").val().trim();
        if(username){
            usernamePage.addClass("d-none");
            chatPage.removeClass("d-none");
            var socket = new SockJS("/notificaciones");
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected, onError)
        }
        event.preventDefault();
    }

    function onConnected(){
        stompClient.subscribe("/topic/public", onMessageReceived);
        stompClient.send("/app/chat.addUser", {}, JSON.stringify({
        envio: username, tipo: 'UNIR'}));
        connectingElement.addClass("d-none");
    }
    function onError(){
        connectingElement.text("No se pudo establecer conexión con el servidor WebSocket");
        connectingElement.css('color', 'red');
    }

    function onMessageReceived(payload){
        var message = JSON.parse(payload.body);
        var messageElement = $("<li>").addClass("list-group-item");
        if(message.tipo === 'UNIR'){
            messageElement.addClass("event-message").text(message.envio
                +" se ha unido al chat");
        }else if (message.tipo === 'DEJAR'){
            messageElement.addClass("event-message").text(message.envio
                                 +" se ha retirado del chat");
        }else{
            var usernameElement = $("<strong>").text(message.envio);
            var textElement = $("<span>").text(message.contenido);
            messageElement.append(usernameElement).append(textElement);
        }
        messageArea.append(messageElement);
        messageArea.scrollTop(messageArea[0].scrollHeigth)
    }
    function enviarMensaje(event){
        var messageContent = $("#message").val().trim();
        if(messageContent && stompClient){
            var chatMessage = {
                envio: username,
                contenido: messageContent,
                tipo: "CHAT"
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            $("#message").val("")
        }
        event.preventDefault();
    }
    usernameForm.on("submit", conectarUsuario);
    messageForm.on("submit", enviarMensaje);

});