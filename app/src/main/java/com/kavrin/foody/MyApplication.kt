package com.kavrin.foody

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp // Annotation for marking the Application class where the Dagger components should be generated
class MyApplication : Application() {
}