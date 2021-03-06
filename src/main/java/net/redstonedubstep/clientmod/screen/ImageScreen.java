package net.redstonedubstep.clientmod.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class ImageScreen extends Screen {
	private final ResourceLocation background;
	int imageWidth;
	int screenWidth;
	int imageHeight;
	int screenHeight;
	int offset = 0;

	public ImageScreen(String name, int width, int height, String backgroundPath) {
		this(name, width, width, height, height, backgroundPath);
	}

	public ImageScreen(String name, int width, int screenWidth, int height, int screenHeight, String backgroundPath) {
		super(new TranslationTextComponent(name));

		background = new ResourceLocation(backgroundPath);
		this.height = height;
		this.imageWidth = width;
		this.screenWidth = screenWidth;
		this.imageHeight = height;
		this.screenHeight = screenHeight;
	}

	@Override
	public void init(){
		addButton(new ExtendedButton((width+ screenWidth)/2-10, (height- imageHeight)/2, 10, 10, new StringTextComponent("X"), this::closeScreen));
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks){
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(background);
		RenderSystem.enableAlphaTest();
		blit(matrix, (width - screenWidth) / 2, (height - imageHeight)/2, offset, 0, screenWidth, imageHeight, imageWidth, imageHeight);
		RenderSystem.disableAlphaTest();

		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		super.mouseScrolled(mouseX, mouseY, delta);

		if (screenWidth != imageWidth) {
			switch ((int)Math.signum(delta)) {
				case -1: offset += 24; break;
				case 1: offset -= 24; break;
			}
		}
		return true;
	}

	public void closeScreen(Button button) {
		this.closeScreen();
	}
}
