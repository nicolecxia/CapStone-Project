import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cocygo.R
import com.example.cocygo.databinding.FragmentLanguageBinding
import java.util.*

class LanguageFragment : Fragment() {
    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)

        // Get current language and update UI
        val currentLanguage = getSavedLanguage()
        updateUI(currentLanguage)

        // Set language change listener
        binding.switchLanguageButton.setOnClickListener {
            val newLanguage = if (currentLanguage == "en") "fr" else "en"
            switchLanguage(newLanguage)
        }

        return binding.root
    }

    private fun switchLanguage(language: String) {
        // Save the new language preference
        sharedPreferences.edit().putString("App_Lang", language).apply()

        // Update the app's locale
        updateLocale(language)

        // Immediately update UI components
        updateUI(language)

        // Optional: Reload the fragment to reflect the changes
        reloadFragment()
    }

    private fun updateLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        // Update the resources to reflect the new locale
        val config = Configuration()
        config.setLocale(locale)  // Update locale
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }

    private fun reloadFragment() {
        // Reload the current fragment to update the UI
        parentFragmentManager.beginTransaction()
            .replace(R.id.language_fragment_container, LanguageFragment())
            .commitAllowingStateLoss()
    }

    private fun getSavedLanguage(): String {
        return sharedPreferences.getString("App_Lang", "en") ?: "en"
    }

    private fun updateUI(language: String) {
        // Update the button image based on the selected language
        if (language == "en") {
            binding.switchLanguageButton.setImageResource(R.drawable.uk)  // UK flag for English
            // Set the content description for English
            binding.switchLanguageButton.contentDescription = "EN"
        } else {
            binding.switchLanguageButton.setImageResource(R.drawable.france)  // French flag for French
            // Set the content description for French
            binding.switchLanguageButton.contentDescription = "FR"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
