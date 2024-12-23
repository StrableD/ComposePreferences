// Top-level build file where you can add configuration options common to all subprojects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.build.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.compose.compiler.gradle.plugin)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins{
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.gradleup.nmcp)
}
