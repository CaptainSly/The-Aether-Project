package io.azraein.aether.alerts;

import io.azraein.aether.utils.AetherConstants;
import io.azraein.aether.utils.AetherFXConstants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AboutAlert extends Alert {

	private final String JFXVersion = "21.0.2"; // JavaFX
	private final String CFXVersion = "11.2.0"; // ControlsFX
	private final String ikonliMatVersion = "12.3.1"; // Ikonli Material
	private final String gsonVersion = "2.10.1"; // GSON
	private final String ini4jVersion = "0.5.4"; // Ini4j
	private final String desktopPaneVersion = "0.15.0"; // DesktopPaneFX

	public AboutAlert() {
		super(AlertType.NONE);

		this.setTitle("About The Aether Project");
		this.setHeaderText("AetherFX Version " + AetherFXConstants.AETHER_FX_VERSION + "\n" + "\nLibraries: \n"
				+ "AetherLink Version " + AetherConstants.AETHER_LIBRARY_VERSION + "\n" + "JavaFX " + JFXVersion + "\n"
				+ "ControlsFX " + CFXVersion + "\n" + "Ikonli/MaterialDesign2 " + ikonliMatVersion + "\n" + "Gson "
				+ gsonVersion + "\n" + "DesktopPaneFX " + desktopPaneVersion + "\n" + "Ini4j " + ini4jVersion + "\n");

		this.setContentText(
				"The Aether Project is a Call of Cthulhu Prop, that allows the Keeper to create and facilitate custom data to players through a Desktop and Mobile App. Allowing for a variety of different play styles.");

		this.getButtonTypes().add(ButtonType.OK);
	}

}
