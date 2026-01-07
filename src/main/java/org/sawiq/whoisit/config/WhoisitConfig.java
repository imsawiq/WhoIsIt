package org.sawiq.whoisit.config;

import net.fabricmc.loader.api.FabricLoader;
import org.sawiq.whoisit.Whoisit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class WhoisitConfig {

    public static final String ENABLED_OWN_NAME_KEY = "enabled_own_name";
    public static final String ENABLED_OTHER_PLAYERS_NAME_KEY = "enabled_other_players_name";
    public static final String REVEAL_INVISIBLE_PLAYERS_KEY = "reveal_invisible_players";

    public static boolean enabledOwnName = false;
    public static boolean enabledOtherPlayersName = true;

    public static boolean revealInvisiblePlayers = false;

    public static boolean enabledOwnNameDefault = false;
    public static boolean enabledOtherPlayersNameDefault = true;
    public static boolean revealInvisiblePlayersDefault = false;

    public static boolean defaultedBool(String propertyBool, boolean defaultBool) {
        if (propertyBool == null) {
            return defaultBool;
        }
        return propertyBool.equalsIgnoreCase("true");
    }

    public static void writeTo(Properties properties) {
        properties.setProperty(ENABLED_OWN_NAME_KEY, Boolean.toString(enabledOwnName));
        properties.setProperty(ENABLED_OTHER_PLAYERS_NAME_KEY, Boolean.toString(enabledOtherPlayersName));
        properties.setProperty(REVEAL_INVISIBLE_PLAYERS_KEY, Boolean.toString(revealInvisiblePlayers));
    }

    public static void readFrom(Properties properties) {
        enabledOwnName = defaultedBool(properties.getProperty(ENABLED_OWN_NAME_KEY), enabledOwnNameDefault);
        enabledOtherPlayersName = defaultedBool(
                properties.getProperty(ENABLED_OTHER_PLAYERS_NAME_KEY),
                enabledOtherPlayersNameDefault
        );
        revealInvisiblePlayers = defaultedBool(
                properties.getProperty(REVEAL_INVISIBLE_PLAYERS_KEY),
                revealInvisiblePlayersDefault
        );
    }

    public static void save() {
        Properties properties = new Properties();
        writeTo(properties);

        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("whoisit.properties");

        if (!Files.exists(configPath)) {
            try {
                Files.createFile(configPath);
            } catch (IOException e) {
                Whoisit.LOGGER.error("Failed to create configuration file!", e);
                return;
            }
        }

        try (var out = Files.newOutputStream(configPath)) {
            properties.store(out, "Configuration file for Who is it");
        } catch (IOException e) {
            Whoisit.LOGGER.error("Failed to write to configuration file!", e);
        }
    }

    public static void load() {
        Properties properties = new Properties();
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("whoisit.properties");

        if (!Files.exists(configPath)) {
            try {
                Files.createFile(configPath);
                save();
            } catch (IOException e) {
                Whoisit.LOGGER.error("Failed to create configuration file!", e);
                return;
            }
        }

        try (var in = Files.newInputStream(configPath)) {
            properties.load(in);
        } catch (IOException e) {
            Whoisit.LOGGER.error("Failed to read configuration file!", e);
            return;
        }

        readFrom(properties);
    }
}
