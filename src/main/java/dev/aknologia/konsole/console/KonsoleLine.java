package dev.aknologia.konsole.console;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class KonsoleLine<T> {
    private final int creationTick;
    private final T text;
    private final int id;
    private final long timestamp;
    private boolean newline = false;

    public KonsoleLine(int parentId, T text) {
        this.creationTick = 0;
        this.id = parentId;
        this.timestamp = 0L;
        this.text = text;
        this.newline = true;
    }

    public KonsoleLine(int i, T object, int j, long ml) {
        this.text = object;
        this.creationTick = i;
        this.id = j;
        this.timestamp = ml;
    }

    public T getText() {
        return this.text;
    }

    public int getCreationTick() {
        return this.creationTick;
    }

    public int getId() {
        return this.id;
    }

    public long getTimestamp() { return this.timestamp; }

    public boolean isNewline() { return this.newline; }
}
