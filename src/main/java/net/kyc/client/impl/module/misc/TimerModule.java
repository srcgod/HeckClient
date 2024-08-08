package net.kyc.client.impl.module.misc;

import net.kyc.client.api.config.Config;
import net.kyc.client.api.config.setting.BooleanConfig;
import net.kyc.client.api.config.setting.NumberConfig;
import net.kyc.client.api.event.EventStage;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import net.kyc.client.impl.event.TickEvent;
import net.kyc.client.impl.event.render.TickCounterEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.init.Modules;

import java.text.DecimalFormat;

/**
 * @author linus
 * @since 1.0
 */
public class TimerModule extends ToggleModule {
    //
    Config<Float> ticksConfig = new NumberConfig<>("Ticks", "The game tick speed", 0.1f, 2.0f, 50.0f);
    Config<Boolean> tpsSyncConfig = new BooleanConfig("TPSSync", "Syncs game tick speed to server tick speed", false);
    //
    private float prevTimer = -1.0f;
    private float timer = 1.0f;

    /**
     *
     */
    public TimerModule() {
        super("Timer", "Changes the client tick speed", ModuleCategory.MISC);
    }

    @Override
    public String getModuleData() {
        DecimalFormat decimal = new DecimalFormat("0.0#");
        return decimal.format(timer);
    }

    @Override
    public void toggle(boolean b) {
        Modules.SPEED.setPrevTimer();
        if (Modules.SPEED.isUsingTimer()) {
            return;
        }
        super.toggle(b);
    }

    @Override
    public void onUpdate() {

    }

    @EventListener
    public void onTick(TickEvent event) {
        if (event.getStage() == EventStage.PRE) {
            if (Modules.SPEED.isUsingTimer()) {
                return;
            }
            if (tpsSyncConfig.getValue()) {
                timer = Math.max(Managers.TICK.getTpsCurrent() / 20.0f, 0.1f);
                return;
            }
            timer = ticksConfig.getValue();
        }
    }

    @EventListener
    public void onTickCounter(TickCounterEvent event) {
        if (timer != 1.0f) {
            event.cancel();
            event.setTicks(timer);
        }
    }

    /**
     * @return
     */
    public float getTimer() {
        return timer;
    }

    /**
     * @param timer
     */
    public void setTimer(float timer) {
        prevTimer = this.timer;
        this.timer = timer;
    }

    public void resetTimer() {
        if (prevTimer > 0.0f) {
            this.timer = prevTimer;
            prevTimer = -1.0f;
        }
    }
}
