package com.teamresourceful.resourcefulbees.api.honeydata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefulbees.utils.color.Color;

public class HoneyColor {

    public static final Codec<HoneyColor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Color.CODEC.fieldOf("color").orElse(Color.DEFAULT).forGetter(HoneyColor::getColor),
            Codec.BOOL.fieldOf("isRainbow").orElse(false).forGetter(HoneyColor::isRainbow)
    ).apply(instance, HoneyColor::new));

    public static final HoneyColor DEFAULT = new HoneyColor(Color.DEFAULT, false);

    private final Color color;
    private final boolean isRainbow;

    public HoneyColor(Color color, boolean isRainbow) {
        this.color = color;
        this.isRainbow = isRainbow;
    }

    public Color getColor() {
        return color;
    }

    public boolean isRainbow() {
        return isRainbow;
    }
}