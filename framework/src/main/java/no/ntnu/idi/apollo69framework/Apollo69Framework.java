package no.ntnu.idi.apollo69framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.DeviceInfo;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInQueue;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.PlayerMatchmade;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;

public class Apollo69Framework {
    private static final class MessageClassListHolder {
        private static final List<Class> CLASSES_SINGLETON = Collections.unmodifiableList(Arrays.asList(
                ArrayList.class,
                DeviceInfo.class,
                PlayerInput.class,
                ServerMessage.class,
                PlayerInQueue.class,
                PlayerMatchmade.class,
                UpdateMessage.class
        ));
    }

    public static List<Class> getMessageClasses() {
        return MessageClassListHolder.CLASSES_SINGLETON;
    }
}
