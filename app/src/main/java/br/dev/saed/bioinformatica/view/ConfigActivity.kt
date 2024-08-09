package br.dev.saed.bioinformatica.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.dev.saed.bioinformatica.R
import br.dev.saed.bioinformatica.model.utils.Config
import br.dev.saed.bioinformatica.model.utils.ConfigManager
import br.dev.saed.bioinformatica.model.utils.dataStore
import br.dev.saed.bioinformatica.model.utils.host
import br.dev.saed.bioinformatica.model.utils.porta
import br.dev.saed.bioinformatica.model.utils.timeout
import br.dev.saed.bioinformatica.view.components.ButtonDefault
import br.dev.saed.bioinformatica.view.components.TextInput
import br.dev.saed.bioinformatica.view.ui.theme.BioInformaticaTheme
import br.dev.saed.bioinformatica.viewmodel.ScriptViewModel
import kotlinx.coroutines.flow.map

class ConfigActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        carregarConfiguracoes()
        setContent {
            BioInformaticaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConfigApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun carregarConfiguracoes() {
        dataStore.data.map { preferences ->
            val hostConfig = preferences[host] ?: ""
            val portConfig = preferences[porta] ?: 0
            val timeoutConfig = preferences[timeout] ?: 1
            ConfigManager.config = Config(hostConfig, portConfig, timeoutConfig)
        }
    }

    fun finishActivity() {
        finish()
    }
}

@Composable
fun ConfigApp(modifier: Modifier) {
    var host by remember { mutableStateOf("") }
    var port by remember { mutableStateOf(0) }
    var timeout by remember { mutableStateOf(1) }
    val context = LocalContext.current

    Column(modifier = modifier.padding(16.dp)) {
        TextInput(
            hint = stringResource(id = R.string.edit_host),
            text = ConfigManager.config?.host ?: ""
        ) {
            host = it
        }
        TextInput(
            hint = stringResource(id = R.string.edit_porta),
            text = ConfigManager.config?.port.toString()
        ) {
            port = it.toInt()
        }
        TextInput(
            hint = stringResource(id = R.string.edit_timeout),
            text = ConfigManager.config?.timeout.toString()
        ) {
            timeout = it.toInt()
        }

        ButtonDefault(text = stringResource(id = R.string.btn_salvar), onClick = {
            val viewModel = ScriptViewModel()
            viewModel.salvarConfiguracoes(host, port, timeout)
            (context as ConfigActivity).finishActivity()
        })
    }
}

@Preview
@Composable
fun ConfigAppPreview() {
    ConfigApp(modifier = Modifier.fillMaxSize())
}