import android.content.Context
import com.orien.bronsonjones.utils.security.EncryptedPreferences

/**
 * Created by android on 27/2/18.
 */
class PreferencesClass {
    companion object {

        var prefs: EncryptedPreferences? = null
        fun initPreferences(context: Context) {
            prefs = EncryptedPreferences(context)
        }
    }
}