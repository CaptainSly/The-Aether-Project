package io.azraein.aether.alerts;

import io.azraein.aether.utils.AetherConstants;
import io.azraein.aether.utils.AetherFXConstants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AboutAlert extends Alert {

	private final String JFXVersion = "21.0.2"; // JavaFX
	private final String MFXVersion = "11.17.0"; // MaterialFX
	private final String AFXVersion = "1.3.0"; // AnimateFX
	private final String CFXVersion = "11.2.0"; // ControlsFX
	private final String ikonliMatVersion = "12.3.1"; // Ikonli Material

	public AboutAlert() {
		super(AlertType.NONE);

		this.setTitle("About The Aether Project");
		this.setHeaderText("AetherFX Version " + AetherFXConstants.AETHER_FX_VERSION + "\n" + "\nLibraries: \n"
				+ "AetherLink Version " + AetherConstants.AETHER_LIBRARY_VERSION + "\n" + "JavaFX " + JFXVersion + "\n"
				+ "MaterialFX " + MFXVersion + "\n" + "AnimateFX " + AFXVersion + "\n" + "ControlsFX " + CFXVersion
				+ "\n" + "Ikonli/MaterialDesign2 " + ikonliMatVersion + "\n");

		this.setContentText(
				"The Aether Project is a Call of Cthulhu Prop, that allows the Keeper to create and facilitate custom data to players through a Desktop and Mobile App. Allowing for a variety of different play styles.");

		this.getButtonTypes().add(ButtonType.OK);
	}

}
