plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.native.cocoapods)
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.serialization)
  id("maven-publish")
}

group = "com.github.smuzani"
version = "0.1.1"

kotlin {
  androidTarget()
  iosX64()
  iosArm64()
  iosSimulatorArm64()

  cocoapods {
    summary = "Some description for the Shared Module"
    homepage = "Link to the Shared Module homepage"
    version = "1.0"
    ios.deploymentTarget = "16.0"
    podfile = project.file("../iosApp/Podfile")
    framework {
      baseName = "shared"
      isStatic = true
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.bundles.ktor)
        implementation(libs.kotlin.date.time)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
        implementation(libs.assertk)
        implementation(libs.turbine)
      }
    }
    val androidMain by getting {
      dependencies {
        implementation(libs.ktor.android)
      }
    }
    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosMain by creating {
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)

      dependencies {
        implementation(libs.ktor.ios)
      }
    }
    val iosX64Test by getting
    val iosArm64Test by getting
    val iosSimulatorArm64Test by getting
    val iosTest by creating {
      dependsOn(commonTest)
      iosX64Test.dependsOn(this)
      iosArm64Test.dependsOn(this)
      iosSimulatorArm64Test.dependsOn(this)
    }
  }

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = "17"
      }
    }
    publishLibraryVariants("release", "debug")
  }

  // disable warning on expect/actual
  targets.configureEach {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          freeCompilerArgs.add("-Xexpect-actual-classes")
        }
      }
    }
  }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/smuzani/kmp-minimalist-template")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        withType<MavenPublication> {
            groupId = "com.github.smuzani"
            artifactId = "kmp-minimalist-template"
            version = "0.1.1"
        }
    }
}

android {
  namespace = "com.template.kmp"
  compileSdk = 34
  defaultConfig {
    minSdk = 21
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}