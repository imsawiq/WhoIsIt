package org.sawiq.whoisit.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WhoisitConfigScreen extends Screen {

    private final Screen parent;

    public WhoisitConfigScreen(Screen parent) {
        super(Text.translatable("whoisit.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int startY = this.height / 4;

        ButtonWidget ownNameButton = ButtonWidget.builder(
                this.getButtonText("whoisit.config.own_name", WhoisitConfig.enabledOwnName),
                button -> {
                    WhoisitConfig.enabledOwnName = !WhoisitConfig.enabledOwnName;
                    button.setMessage(this.getButtonText("whoisit.config.own_name", WhoisitConfig.enabledOwnName));
                    WhoisitConfig.save();
                }
        ).dimensions(centerX - 155, startY, 310, 20).build();

        ButtonWidget otherPlayersButton = ButtonWidget.builder(
                this.getButtonText("whoisit.config.other_players", WhoisitConfig.enabledOtherPlayersName),
                button -> {
                    WhoisitConfig.enabledOtherPlayersName = !WhoisitConfig.enabledOtherPlayersName;
                    button.setMessage(this.getButtonText("whoisit.config.other_players", WhoisitConfig.enabledOtherPlayersName));
                    WhoisitConfig.save();
                }
        ).dimensions(centerX - 155, startY + 30, 310, 20).build();

        ButtonWidget invisiblePlayersButton = ButtonWidget.builder(
                this.getButtonText("whoisit.config.invisible_players", WhoisitConfig.revealInvisiblePlayers),
                button -> {
                    WhoisitConfig.revealInvisiblePlayers = !WhoisitConfig.revealInvisiblePlayers;
                    button.setMessage(this.getButtonText("whoisit.config.invisible_players", WhoisitConfig.revealInvisiblePlayers));
                    WhoisitConfig.save();
                }
        ).dimensions(centerX - 155, startY + 60, 310, 20).build();

        ButtonWidget doneButton = ButtonWidget.builder(
                ScreenTexts.DONE,
                button -> {
                    WhoisitConfig.save();
                    if (this.client != null) {
                        this.client.setScreen(this.parent);
                    }
                }
        ).dimensions(centerX - 100, this.height - 28, 200, 20).build();

        this.addDrawableChild(ownNameButton);
        this.addDrawableChild(otherPlayersButton);
        this.addDrawableChild(invisiblePlayersButton);
        this.addDrawableChild(doneButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.translatable("whoisit.config.subtitle").formatted(Formatting.GRAY),
                this.width / 2,
                35,
                0xAAAAAA
        );
    }

    private Text getButtonText(String key, boolean enabled) {
        Text option = Text.translatable(key);
        Text status = enabled
                ? Text.translatable("whoisit.config.enabled").formatted(Formatting.GREEN)
                : Text.translatable("whoisit.config.disabled").formatted(Formatting.RED);

        return Text.literal("").append(option).append(": ").append(status);
    }

    @Override
    public void close() {
        WhoisitConfig.save();
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
