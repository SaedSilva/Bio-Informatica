package br.dev.saed.bioinformatica.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TextInput(modifier: Modifier = Modifier, text: String = "", hint: String = "", onValueChange: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier.padding(16.dp).fillMaxWidth(),
        placeholder = { Text(text = hint) }
    )
}

@Preview
@Composable
fun TextInputPreview() {
    TextInput(hint = "Teste"){}
}