package cn.crtlprototypestudios.beacon_color_selector.client;

import cn.crtlprototypestudios.beacon_color_selector.client.commands.BeaconColorCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class BeaconColorSelectorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.print("Beacon Color Selector Client-Side Initialized!");
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            BeaconColorCommand.register(dispatcher);
        }));
    }
}
