package net.kyc.client.impl.module.client;


import net.kyc.client.api.module.ModuleCategory;
import net.kyc.client.api.module.ToggleModule;
import static net.kyc.client.RPC.startRPC;
import static net.kyc.client.RPC.stopRPC;


public class DRPC extends ToggleModule {
    public DRPC() {

        super("RPC", "lolol", ModuleCategory.CLIENT);
        enable();
    }

    @Override
    public void onEnable() {
        startRPC();
    }

    @Override
    public void onDisable() {
        stopRPC();
    }

    @Override
    public void onUpdate() {
    }

}





