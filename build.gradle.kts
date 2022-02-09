buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
    dependencies {
        classpath(Classpath.GRADLE_TOOLS)
        classpath(Classpath.KOTLIN_GRADLE_TOOLS)
        classpath(Classpath.GMS)
        classpath(Classpath.NAVIGATION_GRADLE)
        classpath(Classpath.CRASHLYTICS)
        classpath(Classpath.HILT)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
