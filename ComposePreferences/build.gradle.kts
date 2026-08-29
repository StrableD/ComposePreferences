import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.gradleup.nmcp)
}

kotlin {
    androidLibrary {
        namespace = "com.strabled.composepreferences"
        compileSdk = libs.versions.sdk.compile.get().toInt()
        minSdk = libs.versions.sdk.min.get().toInt()
    }

    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    macosArm64()

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "compose-preferences"
            isStatic = true
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("noJs") {
                withJvm()
                withAndroidTarget()
            }
            group("withJs") {
                withJs()
                withWasmJs()
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)

            implementation(libs.kotlin.serialization.json)
        }

        val noJsMain by getting {
            dependencies {
                implementation(libs.androidx.datastore.preferences)
            }
        }

        val androidMain by getting {
            dependsOn(noJsMain)
        }

        val nativeMain by getting {
            dependencies {
                implementation(libs.androidx.datastore.preferences)
            }
        }

        val withJsMain by getting {
            dependencies {
                implementation(libs.kotlinx.datetime)
            }
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}