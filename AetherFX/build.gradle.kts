import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
plugins {
	`java`
	id("org.openjfx.javafxplugin") version "0.1.0"
	id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.azraein"
version = "0.1.0"


javafx {
    version = "21.0.2"
    modules = listOf("javafx.controls", "javafx.media")
}

dependencies {
	implementation(project(":AetherLink"))
	implementation("org.controlsfx:controlsfx:11.2.0")
	implementation("org.kordamp.desktoppanefx:desktoppanefx-core:0.15.0")
	implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
	implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
}

tasks.withType<ShadowJar> {
	
	archiveBaseName.set("aether")
	archiveClassifier.set("fx")
	archiveVersion.set("${version}")
	
	manifest {
        attributes["Main-Class"] = "io.azraein.aether.Main"
    }
    
     mergeServiceFiles()
     
	duplicatesStrategy = DuplicatesStrategy.INCLUDE

    // Include the JavaFX runtime dependencies in the JAR
    from(sourceSets.main.get().output)
	from(project.configurations.compileClasspath)
}