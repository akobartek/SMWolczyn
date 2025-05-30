package pl.kapucyni.wolczyn.app

import App
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )

        setContent {
            App()
        }
    }

    override fun onResume() {
        super.onResume()
        (application as? WolczynApplication)?.currentActivity = this@MainActivity
    }

    override fun onStop() {
        super.onStop()
        (application as? WolczynApplication)?.currentActivity = null
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}