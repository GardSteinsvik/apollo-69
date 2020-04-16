package no.ntnu.idi.apollo69server.network;

import com.esotericsoftware.kryonet.FrameworkMessage;

import java.util.HashMap;
import java.util.Map;

public class MessageHandlerDelegator {

    private final Map<Class, MessageHandler> handlers = new HashMap<>();

    public <M> void registerHandler(final MessageHandler<M> handler, final Class<M> messageClass) {
        if (handlers.containsKey(messageClass)) {
            final String errorMessage = String.format("Tried to register handler \"%s\","
                            + "but there is already a handler registered for \"%s\": \n\"%s\"",
                    handler.getClass().getCanonicalName(),
                    messageClass.getCanonicalName(),
                    handlers.get(messageClass).getClass().getCanonicalName());
            throw new IllegalArgumentException(errorMessage);
        }

        handlers.put(messageClass, handler);
    }

    public void unregisterHandler(final Class messageClass) {
        handlers.remove(messageClass);
    }

    public void handleMessage(final PlayerConnection connection, final Object message) {
        final MessageHandler messageHandler = handlers.get(message.getClass());
        if (messageHandler != null) {
            messageHandler.handle(connection, message);
        } else {
            if (!(message instanceof FrameworkMessage.KeepAlive)) {
//                System.out.println("Unhandled message: " + message);
            }
        }
    }
}
