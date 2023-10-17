package ch.com.findrealestate

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

internal fun Project.configureKotlin(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_18
            targetCompatibility = JavaVersion.VERSION_18
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_18.toString()
        }
    }
}

fun CommonExtension<*, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}