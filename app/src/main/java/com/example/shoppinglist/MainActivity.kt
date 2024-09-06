package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.ui.theme.ShoppingListTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class) // TopAppBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                val items = remember { mutableStateListOf<String>() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text("Shopping list") }) }
                )
                { innerPadding ->
                    ShoppingList(
                        items = items,
                        onAddItem = { items.add(it) },
                        onDeleteItem = { items.remove(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingList(
    items: List<String> = emptyList(),
    modifier: Modifier = Modifier,
    onAddItem: (String) -> Unit = {},
    onDeleteItem: (String) -> Unit = {}
) {
    //val items = remember { mutableStateListOf<String>() }
    // was hoisted to MainActivity for better preview
    var newItem by remember { mutableStateOf("") }
    Column(modifier = modifier.padding(10.dp)) {
        Row(verticalAlignment = CenterVertically) {
            var inputError by remember { mutableStateOf(false) }
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = newItem,
                label = { Text("New Item") },
                onValueChange = { newItem = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                supportingText = {
                    if (inputError)
                        Text("This field is required", color = Color.Red)
                }
            )
            Button(
                onClick = {
                    if (newItem.isBlank()) {
                        inputError = true
                    } else {
                        inputError = false
                        onAddItem(newItem)
                        newItem = ""
                    }
                },
                modifier = Modifier.padding(start = 5.dp),
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Add")
            }
        }
        LazyColumn {
            items(items) { item ->
                ItemsCard(item = item) { onDeleteItem(item) }
            }
        }
    }
}

@Composable
fun ItemsCard(
    item: String, modifier: Modifier = Modifier,
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.padding(2.dp),
            verticalAlignment = CenterVertically
        ) {
            Text(text = item, modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onDelete) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Preview
@Composable
fun ItemsCardPreview() {
    ShoppingListTheme {
        ItemsCard(item = "Item 1")
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListPreview() {
    ShoppingListTheme {
        val items = List(3) { "Item $it" }
        ShoppingList(items = items)
    }
}