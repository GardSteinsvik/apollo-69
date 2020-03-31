package no.ntnu.idi.apollo69.game_engine;

import com.badlogic.ashley.core.ComponentMapper;

import no.ntnu.idi.apollo69.game_engine.components.AttackingComponent;
import no.ntnu.idi.apollo69.game_engine.components.DamageComponent;
import no.ntnu.idi.apollo69.game_engine.components.DimensionComponent;
import no.ntnu.idi.apollo69.game_engine.components.HealthComponent;
import no.ntnu.idi.apollo69.game_engine.components.PositionComponent;
import no.ntnu.idi.apollo69.game_engine.components.PowerupComponent;
import no.ntnu.idi.apollo69.game_engine.components.RotationComponent;
import no.ntnu.idi.apollo69.game_engine.components.BoosterComponent;
import no.ntnu.idi.apollo69.game_engine.components.SpriteComponent;
import no.ntnu.idi.apollo69.game_engine.components.VelocityComponent;

public class Mappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<RotationComponent> rotation = ComponentMapper.getFor(RotationComponent.class);
    public static final ComponentMapper<DimensionComponent> dimension = ComponentMapper.getFor(DimensionComponent.class);
    public static final ComponentMapper<HealthComponent> health = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);
    public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<BoosterComponent> booster = ComponentMapper.getFor(BoosterComponent.class);
    public static final ComponentMapper<AttackingComponent> attack = ComponentMapper.getFor(AttackingComponent.class);
    public static final ComponentMapper<PowerupComponent> powerup = ComponentMapper.getFor(PowerupComponent.class);
}
