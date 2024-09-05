package com.jedi.lightsabershop;

public enum Component {
    BLADE_EMITTER,
    FOCUSING_LENS,
    CYCLING_FIELD_ENERGIZERS,
    MAIN_HILT,
    KYBER_CRYSTAL,
    LIGHTSABER_ENERGY_CORE,
    HAND_GRIP,
    INERT_POWER_INSULATOR,
    POMMEL_CAP;


    public String getFormattedName() {
        String name = this.name();
        String formattedName = name.replace("_", " ");
        return formattedName.substring(0, 1).toUpperCase() + formattedName.substring(1).toLowerCase();
    }

    public static String[] getFormattedNames() {
        Component[] components = Component.values();
        String[] formattedNames = new String[components.length];
        for (int i = 0; i < components.length; i++) {
            formattedNames[i] = components[i].getFormattedName();
        }
        return formattedNames;
    }
}
