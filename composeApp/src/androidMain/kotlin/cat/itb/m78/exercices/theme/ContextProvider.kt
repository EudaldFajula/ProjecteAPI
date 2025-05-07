package cat.itb.m78.exercices.theme

import android.content.Context
import androidx.startup.Initializer

object ContextProvider {
    lateinit var applicationContext: Context
        private set

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }
}