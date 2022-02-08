plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("tasks.BuildManager") {
            id = "com.solar.dev.plugin"
            implementationClass = "tasks.BuildManager"
            version = "1.0.0"
        }
    }
}
// after add id to build.gradle, run terminal :
// ./gradlew clean
// then ./gradlew -q renameApk