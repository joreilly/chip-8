package dev.johnoreilly.chip8.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.beust.chip8.AssemblyLine
import com.beust.chip8.h

/** Disassembly of the loaded rom, for anyone curious what the machine is actually running. */
@Composable
fun DisassemblyPane(
    disassembly: List<AssemblyLine>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        Text(
            text = "DISASSEMBLY",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        LazyColumn(contentPadding = PaddingValues(vertical = 4.dp)) {
            items(disassembly) { line ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = line.counter.h,
                        modifier = Modifier.width(44.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "${line.byte0.h}${line.byte1.h}",
                        modifier = Modifier.width(44.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        text = line.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
