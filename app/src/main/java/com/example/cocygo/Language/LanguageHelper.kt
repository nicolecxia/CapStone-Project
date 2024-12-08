import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.Locale


import java.util.*

object LanguageHelper {
    fun wrap(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
