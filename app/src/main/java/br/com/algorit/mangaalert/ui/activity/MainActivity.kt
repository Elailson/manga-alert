package br.com.algorit.mangaalert.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.Text
import androidx.ui.core.setContent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text(text = "Hello World!")
        }
    }
//
//    @Composable
//    fun Greeting(name: String) {
//        Text(text = "Hello $name!")
//    }
}