import androidx.compose.ui.window.application
import com.kplc.outage.di.initKoin
import org.koin.core.Koin
import ui.screens.MainScreen

lateinit var koin: Koin

fun main() {
    koin = initKoin(isDebug = false).koin

    return application {
        MainScreen(applicationScope = this)
    }
}
