package net.kyc.client.impl.event.keyboard;

import net.minecraft.client.input.Input;
import net.kyc.client.api.event.Cancelable;
import net.kyc.client.api.event.Event;
import net.kyc.client.api.event.StageEvent;

@Cancelable
public class KeyboardTickEvent extends StageEvent {

    private final Input input;

    public KeyboardTickEvent(Input input) {
        this.input = input;
    }

    public Input getInput() {
        return input;
    }
}
