import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.hilt)
}

configure<ApplicationExtension> {
    namespace = "com.hrk.test_app"

    compileSdk = 37

    defaultConfig {
        applicationId = "com.hrk.test_app"
        minSdk = 28
        targetSdk = 37
        versionCode = 1
        versionName = "1.0.0"

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        @Suppress("UnstableApiUsage")
        bundle {
            language.enableSplit = false
        }
    }

    flavorDimensions += "type"

    productFlavors {
        create("Production") {
            dimension = "type"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/NOTICE.txt"
        }
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
}

@Suppress("UnstableApiUsage")
androidComponents {
    onVariants(selector().all()) { variant ->
        variant.outputs.forEach { output ->
            val finalName = providers.gradleProperty("apkName").orNull ?: "${project.name}-${variant.name}.apk"
            // Use AGP 8.x+ Property API instead of reflection
            output.outputFileName.set(finalName)
        }

        if (variant.buildType == "debug") {
            println("buildDir: ${project.layout.buildDirectory.get().asFile} - ${variant.buildType}")
        }
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // Dependency Injection (hilt)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // JSON
    implementation(libs.gson)

    // Lifecycle
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    // In-App Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
    implementation(libs.kotlinx.coroutines.play.services)

    // Media3 (ExoPlayer)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.common)
}
