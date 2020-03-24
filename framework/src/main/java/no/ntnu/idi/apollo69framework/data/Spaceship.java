package no.ntnu.idi.apollo69framework.data;

import com.badlogic.ashley.core.Entity;

public class Spaceship {

    private Entity entity;

    public Spaceship() {
        entity = new Entity();
    }

    public Entity getEntity() {
        return entity;
    }

}
