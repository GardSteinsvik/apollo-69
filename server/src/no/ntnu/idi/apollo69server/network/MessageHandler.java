package no.ntnu.idi.apollo69server.network;

public interface MessageHandler<M> {
    void handle(PlayerConnection connection, M message);
}
