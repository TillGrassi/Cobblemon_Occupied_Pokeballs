plugins {
    id("fabric-loom") version "1.15.5"
    id("org.jetbrains.kotlin.jvm") version "2.3.20"
}

version = "1.1.0"
group = "com.pokecapsule"

base {
    archivesName.set("cobblemon-occupied-pokeballs")
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.impactdev.net/repository/development/")
    maven("https://api.modrinth.com/maven")
    maven("https://cursemaven.com")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://maven.shedaniel.me/")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.1")
    mappings("net.fabricmc:yarn:1.21.1+build.3:v2")
    modImplementation("net.fabricmc:fabric-loader:0.16.5")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.102.0+1.21.1")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.13.10+kotlin.2.3.20")
    modImplementation("curse.maven:cobblemon-687131:7273170") // Cobblemon 1.7.1 for 1.21.1
    // Jade — drop any Jade jar into libs/ at the project root, it will be picked up automatically
    modCompileOnly(fileTree("libs") { include("*Jade*.jar", "*jade*.jar") })
    modCompileOnly("com.terraformersmc:modmenu:11.0.3")
    modCompileOnly("me.shedaniel.cloth:cloth-config-fabric:15.0.140")
    modLocalRuntime("com.terraformersmc:modmenu:11.0.3")
    modLocalRuntime("me.shedaniel.cloth:cloth-config-fabric:15.0.140")
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.jar {
    from("LICENSE")
}
