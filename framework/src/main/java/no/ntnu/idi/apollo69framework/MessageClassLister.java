package no.ntnu.idi.apollo69framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MessageClassLister {

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
