package no.ntnu.idi.apollo69.controller;

import com.badlogic.ashley.core.ComponentMapper;
import no.ntnu.idi.apollo69.model.component.DamageComponent;
import no.ntnu.idi.apollo69.model.component.DimensionComponent;
import no.ntnu.idi.apollo69.model.component.HealthComponent;
import no.ntnu.idi.apollo69.model.component.PositionComponent;
import no.ntnu.idi.apollo69.model.component.RadiusComponent;
import no.ntnu.idi.apollo69.model.component.RotationComponent;
import no.ntnu.idi.apollo69.model.component.TextureComponent;
import no.ntnu.idi.apollo69.model.component.VelocityComponent;

public class Mappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<RotationComponent> rotation = ComponentMapper.getFor(RotationComponent.class);
    public static final ComponentMapper<DimensionComponent> dimension = ComponentMapper.getFor(DimensionComponent.class);
    public static final ComponentMapper<RadiusComponent> radius = ComponentMapper.getFor(RadiusComponent.class);
    public static final ComponentMapper<HealthComponent> health = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
}
