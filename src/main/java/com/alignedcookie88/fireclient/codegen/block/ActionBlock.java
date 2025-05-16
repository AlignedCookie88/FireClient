package com.alignedcookie88.fireclient.codegen.block;

public class ActionBlock extends CodeBlock {

    private final Type type;
    private final String name;

    public ActionBlock(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String getType() {
        return type.type;
    }

    @Override
    public String getName() {
        return name;
    }

    public enum Type {
        PLAYER_EVENT("event"),
        PLAYER_ACTION("player_action"),
        ENTITY_EVENT("entity_event"),
        ENTITY_ACTION("entity_action"),
        SET_VARIABLE("set_var"),
        GAME_ACTION("game_action"),
        REPEAT("repeat"),
        CONTROL("control");

        final String type;

        Type(String type) {
            this.type = type;
        }
    }
}
