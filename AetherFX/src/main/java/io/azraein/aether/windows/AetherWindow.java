package io.azraein.aether.windows;

import org.kordamp.desktoppanefx.scene.layout.InternalWindow;

import io.azraein.aether.AetherFX;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public abstract class AetherWindow extends InternalWindow {

	protected BorderPane root;
	protected AetherFX parent;

	public AetherWindow(AetherFX parent, String mdiWindowID, Node icon, String title) {
		super(mdiWindowID, icon, title, new BorderPane());
		this.parent = parent;
		root = (BorderPane) this.getContent();
	}
	
}
