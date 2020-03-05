package no.ntnu.idi.apollo69framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.SomeRequest;
import no.ntnu.idi.apollo69framework.network_messages.SomeResponse;

public class Apollo69Framework {
    private static final class MessageClassListHolder {
        private static final List<Class> CLASSES_SINGLETON = Collections.unmodifiableList(Arrays.asList(
                ArrayList.class,
                SomeRequest.class,
                SomeResponse.class
        ));
    }

    public static List<Class> getMessageClasses() {
        return MessageClassListHolder.CLASSES_SINGLETON;
    }
}
