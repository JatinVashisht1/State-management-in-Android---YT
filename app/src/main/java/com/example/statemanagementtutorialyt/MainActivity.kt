package com.example.statemanagementtutorialyt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.statemanagementtutorialyt.ui.theme.StateManagementTutorialYTTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateManagementTutorialYTTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyUi()
                }
            }
        }
    }
}

@Composable
fun MyUi(viewModel: MainViewModel = hiltViewModel()) {

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit){
        val event = viewModel.uiEvent.receiveAsFlow().collect {  uiEvent ->
            when(uiEvent){
                is UiEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(message = uiEvent.message)
            }
        }
    }

    // column is equivalent to linear layout of xml
    // control
    // elements used to have internal state in xml
    // compose offers uni-directional dataflow

    // Column: Places children vertically
    // Row: Places children horizontally
   val textFieldState = viewModel.textFieldState.value
    Scaffold(scaffoldState = scaffoldState) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // blank
            // isNotBlank
            // isNot && length < 5 characters
            // isError : true/false
            // blank: placeholder
            TextField(
                value = textFieldState.textFieldString,
                onValueChange = viewModel::onTextFieldValueChange,
                modifier = Modifier.fillMaxWidth(0.5f),
                isError = textFieldState.isError,
                placeholder = {
                    Text(text = textFieldState.placeholder)
                }
            )
            Button(onClick = viewModel::onSaveButtonClicked) {
                Text("Save")
            }
        }
    }
}

