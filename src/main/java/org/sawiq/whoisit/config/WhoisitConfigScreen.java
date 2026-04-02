package org.sawiq.whoisit.config;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class WhoisitConfigScreen extends Screen {

    private final Screen parent;

    public WhoisitConfigScreen(Screen parent) {
        super(Component.translatable("whoisit.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = this.height / 4;

        Button ownNameButton = Button.builder(
                this.getButtonText("whoisit.config.own_name", WhoisitConfig.enabledOwnName),
                button -> {
                    WhoisitConfig.enabledOwnName = !WhoisitConfig.enabledOwnName;
                    button.setMessage(this.getButtonText("whoisit.config.own_name", WhoisitConfig.enabledOwnName));
                    WhoisitConfig.save();
                }
        ).bounds(centerX - 155, startY, 310, 20).build();

        Button otherPlayersButton = Button.builder(
                this.getButtonText("whoisit.config.other_players", WhoisitConfig.enabledOtherPlayersName),
                button -> {
                    WhoisitConfig.enabledOtherPlayersName = !WhoisitConfig.enabledOtherPlayersName;
                    button.setMessage(this.getButtonText("whoisit.config.other_players", WhoisitConfig.enabledOtherPlayersName));
                    WhoisitConfig.save();
                }
        ).bounds(centerX - 155, startY + 30, 310, 20).build();

        Button invisiblePlayersButton = Button.builder(
                this.getButtonText("whoisit.config.invisible_players", WhoisitConfig.revealInvisiblePlayers),
                button -> {
                    WhoisitConfig.revealInvisiblePlayers = !WhoisitConfig.revealInvisiblePlayers;
                    button.setMessage(this.getButtonText("whoisit.config.invisible_players", WhoisitConfig.revealInvisiblePlayers));
                    WhoisitConfig.save();
                }
        ).bounds(centerX - 155, startY + 60, 310, 20).build();

        Button doneButton = Button.builder(
                CommonComponents.GUI_DONE,
                button -> this.onClose()
        ).bounds(centerX - 100, this.height - 28, 200, 20).build();

        this.addRenderableWidget(ownNameButton);
        this.addRenderableWidget(otherPlayersButton);
        this.addRenderableWidget(invisiblePlayersButton);
        this.addRenderableWidget(doneButton);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        graphics.centeredText(
                this.font,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );

        graphics.centeredText(
                this.font,
                Component.translatable("whoisit.config.subtitle").withStyle(ChatFormatting.GRAY),
                this.width / 2,
                35,
                0xAAAAAA
        );
    }

    private Component getButtonText(String key, boolean enabled) {
        Component option = Component.translatable(key);
        Component status = enabled
                ? Component.translatable("whoisit.config.enabled").withStyle(ChatFormatting.GREEN)
                : Component.translatable("whoisit.config.disabled").withStyle(ChatFormatting.RED);

        return Component.empty().append(option).append(": ").append(status);
    }

    @Override
    public void onClose() {
        WhoisitConfig.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}
