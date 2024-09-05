package com.jedi.lightsabershop;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Item {
    UUID id;
    String name;
    Component component;
    double price;
    @Nullable
    String description;

    public Item(String name, Component component, double price, @Nullable String description) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.component = component;
        this.price = price;
        this.description = description;
    }
}
