package com.example.anative

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.anative.ui.theme.NativeTheme

class ActivityUtama : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFECYCLE_TEST", "onCreate dipanggil")
        setContent {
            HalamanUtama()
        }
    }
    override fun onPause() {
        super.onPause()
        Log.d("LIFECYCLE_TEST", "onPause dipanggil")
    }
    override fun onStop() {
        super.onStop()
        Log.d("LIFECYCLE_TEST", "onStop dipanggil")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("LIFECYCLE_TEST", "onDestroy dipanggil")
    }
}

