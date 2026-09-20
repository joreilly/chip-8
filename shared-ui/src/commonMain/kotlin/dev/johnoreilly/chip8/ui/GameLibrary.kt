package dev.johnoreilly.chip8.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val Games = GameCatalog.filterNot { it.isDemo }
private val Demos = GameCatalog.filter { it.isDemo }

/** Sidebar rom browser used on wide windows. */
@Composable
fun GameLibrary(
    selectedGame: Game,
    onGameSelected: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        item { SectionHeader("Games") }
        items(Games) { game ->
            GameRow(game, game == selectedGame, onGameSelected)
        }
        item {
            HorizontalDivider(Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            SectionHeader("Demos")
        }
        items(Demos) { game ->
            GameRow(game, game == selectedGame, onGameSelected)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun GameRow(game: Game, selected: Boolean, onGameSelected: (Game) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
            .clickable { onGameSelected(game) }
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Text(
            text = game.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            },
        )
        Text(
            text = game.credit,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}

/** Rom picker for narrow windows, where a permanent sidebar would not fit. */
@Composable
fun CompactGamePicker(
    selectedGame: Game,
    onGameSelected: (Game) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.padding(horizontal = 12.dp)) {
        TextButton(onClick = { expanded = true }) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(selectedGame.title, style = MaterialTheme.typography.labelLarge)
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Choose a game")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 420.dp),
        ) {
            GameCatalog.forEach { game ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(game.title, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = game.credit,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                    onClick = {
                        onGameSelected(game)
                        expanded = false
                    },
                    trailingIcon = {
                        if (game == selectedGame) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                )
            }
        }
    }
}
