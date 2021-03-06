package no.ntnu.idi.apollo69server.game_engine.entity_systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.idi.apollo69framework.network_messages.UpdateMessage;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.AsteroidDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.DimensionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.ExplosionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PickupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PlayerDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PositionDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.PowerupDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.RotationDto;
import no.ntnu.idi.apollo69framework.network_messages.data_transfer_objects.ShotDto;
import no.ntnu.idi.apollo69server.game_engine.components.AsteroidComponent;
import no.ntnu.idi.apollo69server.game_engine.components.BoundsComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ExplosionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69server.game_engine.components.GemComponent;
import no.ntnu.idi.apollo69server.game_engine.components.NetworkPlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PickupComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PlayerComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69server.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69server.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ScoreComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ShieldComponent;
import no.ntnu.idi.apollo69server.game_engine.components.ShotComponent;
import no.ntnu.idi.apollo69server.game_engine.components.VelocityComponent;
import no.ntnu.idi.apollo69server.network.PlayerConnection;

public class SendUpdateSystem extends EntitySystem {

    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> shots;
    private ImmutableArray<Entity> pickups;
    private ImmutableArray<Entity> powerups;
    private ImmutableArray<Entity> asteroids;
    private ImmutableArray<Entity> explosions;
    private float interval;
    private float timeAccumulator = 0f;

    public SendUpdateSystem(int priority, float interval) {
        super(priority);
        this.interval = interval;
    }

    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(Family.all(NetworkPlayerComponent.class).get());
        shots = engine.getEntitiesFor(Family.all(ShotComponent.class).get());
        asteroids = engine.getEntitiesFor(Family.all(AsteroidComponent.class).get());
        pickups = engine.getEntitiesFor(Family.all(PickupComponent.class).get());
        powerups = engine.getEntitiesFor(Family.all(PowerupComponent.class).get());
        explosions = engine.getEntitiesFor(Family.all(ExplosionComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        timeAccumulator += deltaTime;
        if (timeAccumulator >= interval) {
            sendUpdate();
            timeAccumulator = 0f;
        }
    }

    private void sendUpdate() {
        UpdateMessage updateMessage = createUpdateMessage();

        for (Entity player: players) {
            NetworkPlayerComponent networkPlayerComponent = NetworkPlayerComponent.MAPPER.get(player);
            PlayerConnection playerConnection = networkPlayerComponent.getPlayerConnection();

            if (playerConnection.isConnected()) {
                playerConnection.sendTCP(updateMessage);
            }
        }
    }

    private UpdateMessage createUpdateMessage() {
        UpdateMessage updateMessage = new UpdateMessage();

        // PLAYERS
        List<PlayerDto> playerDtoList = new ArrayList<>();
        for (Entity playerEntity: players) {
            PlayerComponent playerComponent = PlayerComponent.MAPPER.get(playerEntity);
            PositionComponent positionComponent = PositionComponent.MAPPER.get(playerEntity);
            RotationComponent rotationComponent = RotationComponent.MAPPER.get(playerEntity);
            VelocityComponent velocityComponent = VelocityComponent.MAPPER.get(playerEntity);
            HealthComponent healthComponent = HealthComponent.MAPPER.get(playerEntity);
            ScoreComponent scoreComponent = ScoreComponent.MAPPER.get(playerEntity);
            float shieldHp = 0;
            if (playerComponent.hasShield()) {
                ShieldComponent shieldComponent = ShieldComponent.MAPPER.get(playerEntity);
                shieldHp = shieldComponent.hp;
            }
            playerDtoList.add(new PlayerDto(
                    playerComponent.getId(),
                    playerComponent.getName(),
                    playerComponent.getSpaceshipType(),
                    velocityComponent.boosting,
                    healthComponent.hp,
                    shieldHp,
                    new PositionDto(positionComponent.position.x, positionComponent.position.y),
                    new RotationDto(rotationComponent.degrees, rotationComponent.x, rotationComponent.y),
                    playerComponent.isVisible(),
                    scoreComponent.score
            ));
        }
        updateMessage.setPlayerDtoList(playerDtoList);

        List<ShotDto> shotDtoList = new ArrayList<>();
        for (Entity shotEntity: shots) {
            PositionComponent positionComponent = PositionComponent.MAPPER.get(shotEntity);
            BoundsComponent boundsComponent = BoundsComponent.MAPPER.get(shotEntity);

            shotDtoList.add(new ShotDto(
                    new PositionDto(positionComponent.position),
                    boundsComponent.circle.radius
            ));
        }
        updateMessage.setShotDtoList(shotDtoList);

        List<AsteroidDto> asteroidDtoList = new ArrayList<>();
        for (Entity asteroidEntity : asteroids){
            PositionComponent positionComponent = PositionComponent.MAPPER.get(asteroidEntity);
            HealthComponent healthComponent = HealthComponent.MAPPER.get(asteroidEntity);
            asteroidDtoList.add(new AsteroidDto(
                    new PositionDto(positionComponent.position.x, positionComponent.position.y),
                    healthComponent.hp
            ));
        }
        updateMessage.setAsteroidDtoList(asteroidDtoList);

        List<ExplosionDto> explosionDtoList = new ArrayList<>();
        for (Entity explosion: explosions) {
            ExplosionComponent explosionComponent = ExplosionComponent.MAPPER.get(explosion);
            PositionComponent positionComponent = PositionComponent.MAPPER.get(explosion);
            BoundsComponent boundsComponent = BoundsComponent.MAPPER.get(explosion);

            int frameNumber = (int) ((System.currentTimeMillis() - explosionComponent.startTime)/100f);

            explosionDtoList.add(new ExplosionDto(
                    frameNumber,
                    new PositionDto(positionComponent.position),
                    new DimensionDto(boundsComponent.dimensions)
            ));
        }
        updateMessage.setExplosionDtoList(explosionDtoList);

        List<PickupDto> pickupDtoList = new ArrayList<>();
        for (Entity pickup : pickups) {
            PositionComponent positionComponent = PositionComponent.MAPPER.get(pickup);
            GemComponent gemComponent = GemComponent.MAPPER.get(pickup);
            pickupDtoList.add(new PickupDto(
                    new PositionDto(positionComponent.position.x, positionComponent.position.y),
                    gemComponent.type
            ));
        }
        updateMessage.setPickupDtoList(pickupDtoList);

        List<PowerupDto> powerupDtoList = new ArrayList<>();
        for (Entity powerup : powerups) {
            PositionComponent positionComponent = PositionComponent.MAPPER.get(powerup);
            PowerupComponent powerupComponent = PowerupComponent.MAPPER.get(powerup);
            powerupDtoList.add(new PowerupDto(
                    new PositionDto(positionComponent.position.x, positionComponent.position.y),
                    powerupComponent.type
            ));
        }
        updateMessage.setPowerupDtoList(powerupDtoList);

        return updateMessage;
    }
}
