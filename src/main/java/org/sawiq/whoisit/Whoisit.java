package org.sawiq.whoisit;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sawiq.whoisit.config.WhoisitConfig;

public class Whoisit implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("whoisit");

    @Override
    public void onInitializeClient() {
        LOGGER.info("[Who is it] Mod initialized");
        WhoisitConfig.load();
    }
}
