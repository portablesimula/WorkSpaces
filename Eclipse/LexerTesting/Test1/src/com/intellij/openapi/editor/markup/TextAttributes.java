package com.intellij.openapi.editor.markup;

import com.intellij.openapi.diagnostic.Logger;
import java.awt.Color;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import org.intellij.lang.annotations.JdkConstants.FontStyle;
import org.jdom.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

public class TextAttributes implements Cloneable {
    private static final Logger LOG = Logger.getInstance(TextAttributes.class);
    private static final AttributesFlyweight DEFAULT_FLYWEIGHT;
    public static final TextAttributes ERASE_MARKER;
    private @NotNull AttributesFlyweight attrs;

    @Contract("!null, !null -> !null")
    public static TextAttributes merge(@Nullable TextAttributes under, @Nullable TextAttributes above) {
        if (under == null) {
            return above;
        } else if (above == null) {
            return under;
        } else {
            TextAttributes attrs = under.clone();
            if (above.getBackgroundColor() != null) {
                attrs.setBackgroundColor(above.getBackgroundColor());
            }

            if (above.getForegroundColor() != null) {
                attrs.setForegroundColor(above.getForegroundColor());
            }

            attrs.setFontType(above.getFontType() | under.getFontType());
            TextAttributesEffectsBuilder.create(under).coverWith(above).applyTo(attrs);
            return attrs;
        }
    }

    public TextAttributes() {
        this(DEFAULT_FLYWEIGHT);
    }

    private TextAttributes(@NotNull AttributesFlyweight attributesFlyweight) {
        this.attrs = attributesFlyweight;
    }

    public TextAttributes(@NotNull Element element) {
        this.readExternal(element);
    }

    @Internal
    public TextAttributes(@NotNull DataInput in) throws IOException {
        this.readExternal(in);
    }

    public TextAttributes(@Nullable Color foregroundColor, @Nullable Color backgroundColor, @Nullable Color effectColor, EffectType effectType, @FontStyle int fontType) {
        this.setAttributes(foregroundColor, backgroundColor, effectColor, (Color)null, effectType, fontType);
    }

    public void copyFrom(@NotNull TextAttributes other) {
        this.attrs = other.attrs;
    }

    public void setAttributes(Color foregroundColor, Color backgroundColor, Color effectColor, Color errorStripeColor, EffectType effectType, @FontStyle int fontType) {
        this.attrs = AttributesFlyweight.create(foregroundColor, backgroundColor, fontType, effectColor, effectType, Collections.emptyMap(), errorStripeColor);
    }

    public boolean isEmpty() {
        return this.getForegroundColor() == null && this.getBackgroundColor() == null && this.getEffectColor() == null && this.getFontType() == 0;
    }

    public @NotNull AttributesFlyweight getFlyweight() {
        return this.attrs;
    }

    public static @NotNull TextAttributes fromFlyweight(@NotNull AttributesFlyweight flyweight) {
        return new TextAttributes(flyweight);
    }

    public Color getForegroundColor() {
        return this.attrs.getForeground();
    }

    public void setForegroundColor(Color color) {
        this.attrs = this.attrs.withForeground(color);
    }

    public Color getBackgroundColor() {
        return this.attrs.getBackground();
    }

    public void setBackgroundColor(Color color) {
        this.attrs = this.attrs.withBackground(color);
    }

    public Color getEffectColor() {
        return this.attrs.getEffectColor();
    }

    public void setEffectColor(Color color) {
        this.attrs = this.attrs.withEffectColor(color);
    }

    public Color getErrorStripeColor() {
        return this.attrs.getErrorStripeColor();
    }

    public void setErrorStripeColor(Color color) {
        this.attrs = this.attrs.withErrorStripeColor(color);
    }

    @Experimental
    public boolean hasEffects() {
        return this.attrs.hasEffects();
    }

    @Experimental
    public void setAdditionalEffects(@NotNull Map<@NotNull EffectType, ? extends @NotNull Color> effectsMap) {
        this.attrs = this.attrs.withAdditionalEffects(effectsMap);
    }

    @Experimental
    public void withAdditionalEffect(@NotNull EffectType effectType, @NotNull Color color) {
        TextAttributesEffectsBuilder.create(this).coverWith(effectType, color).applyTo(this);
    }

    public @Nullable EffectType getEffectType() {
        return this.attrs.getEffectType();
    }

    @Experimental
    public void forEachAdditionalEffect(@NotNull BiConsumer<? super EffectType, ? super Color> consumer) {
        this.attrs.getAdditionalEffects().forEach(consumer);
    }

    @Experimental
    public void forEachEffect(@NotNull BiConsumer<? super EffectType, ? super Color> consumer) {
        this.attrs.getAllEffects().forEach(consumer);
    }

    public void setEffectType(EffectType effectType) {
        this.attrs = this.attrs.withEffectType(effectType);
    }

    @FontStyle
    public int getFontType() {
        return this.attrs.getFontType();
    }

    public void setFontType(@FontStyle int type) {
        if (type < 0 || type > 3) {
            LOG.error("Wrong font type: " + type);
            type = 0;
        }

        this.attrs = this.attrs.withFontType(type);
    }

    public TextAttributes clone() {
        return new TextAttributes(this.attrs);
    }

    public boolean equals(Object obj) {
        return !(obj instanceof TextAttributes) ? false : Objects.equals(this.attrs, ((TextAttributes)obj).attrs);
    }

    public int hashCode() {
        return this.attrs.hashCode();
    }

    public void readExternal(@NotNull Element element) {
        this.attrs = AttributesFlyweight.create(element);
    }

    @Internal
    public void readExternal(@NotNull DataInput in) throws IOException {
        this.attrs = AttributesFlyweight.create(in);
    }

    public void writeExternal(Element element) {
        this.attrs.writeExternal(element);
    }

    @Internal
    public void writeExternal(@NotNull DataOutput out) throws IOException {
        this.attrs.writeExternal(out);
    }

    public String toString() {
        return "[fore=" + this.getForegroundColor() + ", back=" + this.getBackgroundColor() + ", type=" + this.getFontType() + (this.getEffectType() == null ? "" : ", effect=" + this.getEffectType()) + (this.getEffectColor() == null ? "" : ", effect color=" + this.getEffectColor()) + (this.attrs.getAdditionalEffects().isEmpty() ? "" : ", additional=" + this.attrs.getAdditionalEffects()) + (this.getErrorStripeColor() == null ? "" : ", stripe=" + this.getErrorStripeColor()) + "]";
    }

    static {
        DEFAULT_FLYWEIGHT = AttributesFlyweight.create((Color)null, (Color)null, 0, (Color)null, EffectType.BOXED, Collections.emptyMap(), (Color)null);
        ERASE_MARKER = new TextAttributes() {
            public String toString() {
                return "[ERASE_MARKER]";
            }
        };
    }
}
