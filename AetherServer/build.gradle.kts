plugins {
   `java`
	id("org.openjfx.javafxplugin") version "0.1.0"
}


javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.media")
}

dependencies {
	
	implementation(project(":AetherLink"))
	
	// TinyLog2 
	implementation("org.tinylog:tinylog-api:2.6.2")
	implementation("org.tinylog:tinylog-impl:2.6.2")
}
