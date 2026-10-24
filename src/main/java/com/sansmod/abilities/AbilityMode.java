package com.sansmod.abilities;

public enum AbilityMode {
    BONE_THROW("Bone Throw"),
    GASTER_BLASTER("Gaster Blaster"),
    TELEPORT("Teleport");

    private final String name;

    AbilityMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public AbilityMode next() {
        AbilityMode[] values = values();
        return values[(this.ordinal() + 1) % values.length];
    }
}
