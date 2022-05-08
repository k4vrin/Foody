package com.kavrin.foody

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * My application
 *
 * All apps that use Hilt must contain an Application class that is annotated with @HiltAndroidApp.
 * @HiltAndroidApp triggers Hilt's code generation, including a base class for your application that serves as the application-level dependency container.
 *
 * This generated Hilt component is attached to the Application object's lifecycle and provides dependencies to it.
 * Additionally, it is the parent component of the app, which means that other components can access the dependencies that it provides.
 *
 * Note: Because Hilt's code generation needs access to all of the Gradle modules that use Hilt,
 * the Gradle module that compiles your Application class also needs to have all of your Hilt modules
 * and constructor-injected classes in its transitive dependencies.
 */
@HiltAndroidApp // Annotation for marking the Application class where the Dagger components should be generated
class MyApplication : Application() {
}