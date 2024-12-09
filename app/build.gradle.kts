import com.android.build.api.dsl.DefaultConfig
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
}

fun loadLocalProperties(): Properties {
    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { stream ->
            properties.load(stream)
        }
    }
    return properties
}

fun DefaultConfig.addBuildConfigFieldFromProperties(
    properties: Properties,
    key: String,
    defaultValue: String = "",
) {
    val value = properties.getProperty(key) ?: defaultValue
    buildConfigField("String", key, "\"$value\"")
}

android {
    namespace = "com.thoughtworks.voiceassistant.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.thoughtworks.voiceassistant.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = loadLocalProperties()
        addBuildConfigFieldFromProperties(localProperties, "ALI_IVS_ACCESS_KEY")
        addBuildConfigFieldFromProperties(localProperties, "ALI_IVS_ACCESS_KEY_SECRET")
        addBuildConfigFieldFromProperties(localProperties, "ALI_IVS_APP_KEY")
        addBuildConfigFieldFromProperties(localProperties, "PICOVOICE_ACCESS_KEY")
        addBuildConfigFieldFromProperties(localProperties, "VOLCENGINE_APP_ID")
        addBuildConfigFieldFromProperties(localProperties, "VOLCENGINE_ACCESS_TOKEN")
        addBuildConfigFieldFromProperties(localProperties, "VOLCENGINE_ONE_SENTENCE_RECOGNITION_CLUSTER_ID")
        addBuildConfigFieldFromProperties(localProperties, "VOLCENGINE_STREAM_SPEECH_RECOGNITION_CLUSTER_ID")
        addBuildConfigFieldFromProperties(localProperties, "OPENAI_API_KEY")
    }

    sourceSets {
        named("main") {
            jniLibs.srcDirs("libs/iflytek")
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":alibabakit"))
    implementation(project(":baidukit"))
    implementation(project(":picovoicekit"))
    implementation(project(":iflytekkit"))
    implementation(project(":volcenginekit"))
    implementation(project(":openaikit"))

    implementation(libs.gson)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}