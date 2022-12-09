package org.longevityintime.animefacts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.longevityintime.animefacts.R

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
){
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                Modifier.padding(vertical = 10.dp, horizontal = 25.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = message, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                TextButton(onClick =  onDismiss,
                    Modifier
                        .align(Alignment.End)
                        .padding(top = 20.dp)) {
                    Text(text = stringResource(id = R.string.got_it), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}