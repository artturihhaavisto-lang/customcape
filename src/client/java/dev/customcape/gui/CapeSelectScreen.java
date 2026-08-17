package dev.customcape.gui;

import dev.customcape.CapeCatalog;
import dev.customcape.CapeManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class CapeSelectScreen extends Screen {
	private final @Nullable Screen parent;
	private CapeList list;

	public CapeSelectScreen(@Nullable Screen parent) {
		super(Component.translatable("screen.customcape.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		CapeManager.get().reloadCustom();

		int titleWidth = this.font.width(this.title);
		this.addRenderableWidget(new StringWidget(this.width / 2 - titleWidth / 2, 12, titleWidth, 9, this.title, this.font));
		Component hint = Component.translatable("screen.customcape.hint");
		int hintWidth = this.font.width(hint);
		this.addRenderableWidget(new StringWidget(this.width / 2 - hintWidth / 2, this.height - 48, hintWidth, 9, hint, this.font));

		this.list = new CapeList(this.width, this.height - 80, 28, 24);
		this.addRenderableWidget(this.list);

		int buttonWidth = 150;
		int y = this.height - 28;
		this.addRenderableWidget(Button.builder(Component.translatable("screen.customcape.reload"), b -> {
			CapeManager.get().reloadCustom();
			this.rebuildWidgets();
		}).bounds(this.width / 2 - buttonWidth - 4, y, buttonWidth, 20).build());

		this.addRenderableWidget(Button.builder(Component.translatable("screen.customcape.done"), b -> this.onClose())
			.bounds(this.width / 2 + 4, y, buttonWidth, 20)
			.build());
	}

	@Override
	public void onClose() {
		this.minecraft.gui.setScreen(this.parent);
	}

	private class CapeList extends ObjectSelectionList<CapeList.Entry> {
		CapeList(int width, int height, int y, int itemHeight) {
			super(CapeSelectScreen.this.minecraft, width, height, y, itemHeight);
			CapeManager manager = CapeManager.get();
			String selected = manager.config().selectedCape();

			this.addEntry(new Entry(new CapeCatalog.Entry("_hdr_official", "— Official Capes —", CapeCatalog.Kind.OFFICIAL), false));
			for (CapeCatalog.Entry cape : manager.allEntries()) {
				if (cape.kind() == CapeCatalog.Kind.CUSTOM) {
					continue;
				}
				Entry entry = new Entry(cape, true);
				this.addEntry(entry);
				if (cape.id().equals(selected)) {
					this.setSelected(entry);
				}
			}

			this.addEntry(new Entry(new CapeCatalog.Entry("_hdr_custom", "— Custom Capes —", CapeCatalog.Kind.CUSTOM), false));
			var customs = manager.textures().customEntries();
			if (customs.isEmpty()) {
				this.addEntry(new Entry(new CapeCatalog.Entry("_empty", "(none yet — drop PNGs in custom/)", CapeCatalog.Kind.CUSTOM), false));
			} else {
				for (CapeCatalog.Entry cape : customs) {
					Entry entry = new Entry(cape, true);
					this.addEntry(entry);
					if (cape.id().equals(selected)) {
						this.setSelected(entry);
					}
				}
			}
		}

		@Override
		public int getRowWidth() {
			return Math.min(320, this.width - 40);
		}

		private class Entry extends ObjectSelectionList.Entry<Entry> {
			private final CapeCatalog.Entry cape;
			private final boolean selectable;

			Entry(CapeCatalog.Entry cape, boolean selectable) {
				this.cape = cape;
				this.selectable = selectable;
			}

			@Override
			public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
				int color = this.selectable ? 0xFFFFFFFF : 0xFF808080;
				graphics.text(CapeSelectScreen.this.font, this.cape.displayName(), this.getContentX() + 4, this.getContentYMiddle() - 4, color);
			}

			@Override
			public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
				if (!this.selectable) {
					return false;
				}
				CapeList.this.setSelected(this);
				CapeManager.get().select(this.cape.id());
				return true;
			}

			@Override
			public Component getNarration() {
				return Component.literal(this.cape.displayName());
			}
		}
	}
}
