plugins {
    `java-library`
}

dependencies {

	implementation("org.ini4j:ini4j:0.5.4")

	// Gson
	implementation("com.google.code.gson:gson:2.10.1")
	
	// TinyLog2 
	implementation("org.tinylog:tinylog-api:2.6.2")
	implementation("org.tinylog:tinylog-impl:2.6.2")
	
}
