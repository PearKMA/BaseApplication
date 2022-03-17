/**
 * #1: mkdir buildSrc
 * #2: mkdir -p buildSrc/src/main/java
 *
 * option (if not import): import org.gradle.kotlin.dsl.`kotlin-dsl`
 * */

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("tasks.BuildManager") {
            id = "com.base.plugin"
            implementationClass = "tasks.BuildManager"
            version = "1.0.0"
        }
    }
}
// after add id to build.gradle, run terminal :
// ./gradlew clean
// then ./gradlew -q renameApk