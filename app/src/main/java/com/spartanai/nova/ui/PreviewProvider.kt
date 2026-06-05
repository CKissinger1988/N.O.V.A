package com.spartanai.nova.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.spartanai.nova.ui.theme.NOVATheme
import com.spartanai.nova.ui.screens.*

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MainAppPreview() {
    NOVATheme {
        NovaApp()
    }
}

@Preview(showBackground = true)
@Composable
fun TerminalViewPreview() {
    NOVATheme {
        TerminalScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun WirelessViewPreview() {
    NOVATheme {
        WirelessScreen()
    }
}
