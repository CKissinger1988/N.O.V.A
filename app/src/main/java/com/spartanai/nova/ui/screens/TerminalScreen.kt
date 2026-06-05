package com.spartanai.nova.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spartanai.nova.core.NovaOrchestrator

@Composable
fun TerminalScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val terminalOutput by orchestrator.terminalOutput.collectAsState()
    var currentCommand by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(terminalOutput.size) {
        if (terminalOutput.isNotEmpty()) {
            listState.animateScrollToItem(terminalOutput.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Icon(
                imageVector = Icons.Default.VpnLock,
                contentDescription = "SecCom Active",
                tint = Color.Green,
                modifier = Modifier.size(16.dp)
            )
            Text(
                "SecCom: AES-256-GCM (v2.0)",
                color = Color.Green,
                fontSize = 10.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState
        ) {
            items(terminalOutput) { line ->
                Text(
                    text = line,
                    color = Color.Green,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = currentCommand,
            onValueChange = { currentCommand = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(
                color = Color.Green,
                fontFamily = FontFamily.Monospace
            ),
            placeholder = { Text("Enter command...", color = Color.Gray) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Green,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = Color.Green
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    if (currentCommand.isNotBlank()) {
                        orchestrator.executeCommand(currentCommand)
                        currentCommand = ""
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Run",
                        tint = Color.Green
                    )
                }
            }
        )
    }
}
