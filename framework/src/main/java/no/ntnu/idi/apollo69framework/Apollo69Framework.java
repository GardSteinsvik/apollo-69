package no.ntnu.idi.apollo69framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.DeviceInfo;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInQueue;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInput;
import no.ntnu.idi.apollo69framework.network_messages.PlayerInputType;
import no.ntnu.idi.apollo69framework.network_messages.PlayerMatchmade;
import no.ntnu.idi.apollo69framework.network_messages.PlayerSpawn;
import no.ntnu.idi.apollo69framework.network_messages.ServerMessage;
import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.GemType;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PickupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PositionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupType;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.RotationDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.VelocityDto;

public class Apollo69Framework {
    private static final class MessageClassListHolder {
        private static final List<Class> CLASSES_SINGLETON = Collections.unmodifiableList(Arrays.asList(
                ArrayList.class,
                PlayerDto.class,
                PositionDto.class,
                RotationDto.class,
                VelocityDto.class,
                DeviceInfo.class,
                PlayerInput.class,
                PlayerInputType.class,
                PlayerSpawn.class,
                ServerMessage.class,
                PlayerInQueue.class,
                PlayerMatchmade.class,
                UpdateMessage.class,
                PickupDto.class,
                GemType.class,
                PowerupDto.class,
                PowerupType.class
        ));
    }

    public static List<Class> getMessageClasses() {
        return MessageClassListHolder.CLASSES_SINGLETON;
    }
}
