package com.nivelais.combiplanner.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class, init Hilt with the help of the annotation
 */
@HiltAndroidApp
class CombiPlannerApp : Application()