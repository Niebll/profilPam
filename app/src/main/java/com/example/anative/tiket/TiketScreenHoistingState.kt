package com.example.anative.tiket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.anative.tiket.ui.theme.NativeTheme
import kotlinx.coroutines.delay
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class TiketScreenHoistingState : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NativeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TiketParentScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

enum class StatusType {
    INITIAL,
    LOADING,
    SUCCESS,
    ERROR_EMPTY_NAME
}

/**
 * Parent Composable (State Hoisting)
 * Mengelola state:
 * 1. Harga Tiket
 * 2. Jumlah Tiket
 * 3. Nama Pembeli Tiket
 * Serta status pemesanan dengan LaunchedEffect & rememberSaveable.
 */
@Composable
fun TiketParentScreen(modifier: Modifier = Modifier) {
    // 1. State Harga Tiket
    var hargaTiket by rememberSaveable { mutableIntStateOf(25000) }
    // 2. State Jumlah Tiket
    var jumlahTiket by rememberSaveable { mutableIntStateOf(1) }
    // 3. State Nama Pembeli Tiket
    var namaPembeli by rememberSaveable { mutableStateOf("") }

    // Status State untuk proses pemesanan
    var statusType by rememberSaveable { mutableStateOf(StatusType.INITIAL) }

    // LaunchedEffect untuk menangani proses pemesanan asynchronous (delay 2 detik)
    LaunchedEffect(statusType) {
        if (statusType == StatusType.LOADING) {
            delay(2000L) // Simulasi memproses pesanan selama 2 detik
            statusType = StatusType.SUCCESS
        }
    }

    TiketScreenContent(
        hargaTiket = hargaTiket,
        jumlahTiket = jumlahTiket,
        namaPembeli = namaPembeli,
        statusType = statusType,
        onNamaChange = { newNama ->
            namaPembeli = newNama
            if (statusType == StatusType.ERROR_EMPTY_NAME && newNama.isNotBlank()) {
                statusType = StatusType.INITIAL
            }
        },
        onJumlahIncrement = {
            if (statusType != StatusType.LOADING) {
                jumlahTiket++
            }
        },
        onJumlahDecrement = {
            if (statusType != StatusType.LOADING && jumlahTiket > 1) {
                jumlahTiket--
            }
        },
        onPesanClick = {
            if (namaPembeli.trim().isEmpty()) {
                statusType = StatusType.ERROR_EMPTY_NAME
            } else {
                statusType = StatusType.LOADING
            }
        },
        modifier = modifier
    )
}

/**
 * Stateless Composable UI Content
 */
@Composable
fun TiketScreenContent(
    hargaTiket: Int,
    jumlahTiket: Int,
    namaPembeli: String,
    statusType: StatusType,
    onNamaChange: (String) -> Unit,
    onJumlahIncrement: () -> Unit,
    onJumlahDecrement: () -> Unit,
    onPesanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalHarga = hargaTiket * jumlahTiket

    fun formatRupiah(amount: Int): String {
        val symbols = DecimalFormatSymbols(Locale.forLanguageTag("id-ID"))
        val formatter = DecimalFormat("#,###", symbols)
        return "Rp ${formatter.format(amount)}"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar / Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text(
                text = "Pemesanan Tiket",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Input Nama
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Nama",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                OutlinedTextField(
                    value = namaPembeli,
                    onValueChange = onNamaChange,
                    placeholder = {
                        Text(
                            text = "Masukkan nama Anda",
                            color = Color.Gray
                        )
                    },
                    enabled = statusType != StatusType.LOADING,
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1976D2),
                        unfocusedBorderColor = Color(0xFFCCCCCC)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Input Jumlah Tiket
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Jumlah Tiket",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Tombol Kurang (-)
                    Button(
                        onClick = onJumlahDecrement,
                        enabled = statusType != StatusType.LOADING && jumlahTiket > 1,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEF2FA),
                            contentColor = Color.Black,
                            disabledContainerColor = Color(0xFFF5F5F5),
                            disabledContentColor = Color.LightGray
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Kurang",
                            tint = Color.Black
                        )
                    }

                    // Display Jumlah
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = jumlahTiket.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    // Tombol Tambah (+)
                    Button(
                        onClick = onJumlahIncrement,
                        enabled = statusType != StatusType.LOADING,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEF2FA),
                            contentColor = Color.Black,
                            disabledContainerColor = Color(0xFFF5F5F5),
                            disabledContentColor = Color.LightGray
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah",
                            tint = Color.Black
                        )
                    }
                }
            }

            // Informasi Harga Tiket & Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Harga: ${formatRupiah(hargaTiket)} / tiket",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Total: ${formatRupiah(totalHarga)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
            }

            // Tombol Pesan Tiket
            Button(
                onClick = onPesanClick,
                enabled = statusType != StatusType.LOADING,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFA0B2D6),
                    disabledContentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Pesan Tiket",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Status Card
            val (bgColor, textColor, contentText) = when (statusType) {
                StatusType.INITIAL -> Triple(
                    Color(0xFFF3F4F8),
                    Color(0xFF616161),
                    "Status: Silakan pesan tiket"
                )
                StatusType.LOADING -> Triple(
                    Color(0xFFE8F1FF),
                    Color(0xFF1976D2),
                    "Status: Memproses pesanan..."
                )
                StatusType.SUCCESS -> Triple(
                    Color(0xFFE8F5E9),
                    Color(0xFF2E7D32),
                    "Status: Tiket berhasil dipesan!"
                )
                StatusType.ERROR_EMPTY_NAME -> Triple(
                    Color(0xFFFFEBEE),
                    Color(0xFFC62828),
                    "Status: Nama harus diisi"
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                color = bgColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when (statusType) {
                        StatusType.LOADING -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color(0xFF1976D2),
                                strokeWidth = 2.dp
                            )
                        }
                        StatusType.SUCCESS -> {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Berhasil",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        StatusType.ERROR_EMPTY_NAME -> {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        StatusType.INITIAL -> {
                            // Kosong tanpa ikon
                        }
                    }

                    Text(
                        text = contentText,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ==================== PREVIEW FOR 4 STATES ====================

@Preview(showBackground = true, name = "1. Halaman Awal")
@Composable
fun PreviewHalamanAwal() {
    NativeTheme {
        TiketScreenContent(
            hargaTiket = 25000,
            jumlahTiket = 1,
            namaPembeli = "",
            statusType = StatusType.INITIAL,
            onNamaChange = {},
            onJumlahIncrement = {},
            onJumlahDecrement = {},
            onPesanClick = {}
        )
    }
}

@Preview(showBackground = true, name = "2. Proses Memesan (2 detik)")
@Composable
fun PreviewProsesMemesan() {
    NativeTheme {
        TiketScreenContent(
            hargaTiket = 25000,
            jumlahTiket = 1,
            namaPembeli = "Budi Santoso",
            statusType = StatusType.LOADING,
            onNamaChange = {},
            onJumlahIncrement = {},
            onJumlahDecrement = {},
            onPesanClick = {}
        )
    }
}

@Preview(showBackground = true, name = "3. Pesanan Berhasil")
@Composable
fun PreviewPesananBerhasil() {
    NativeTheme {
        TiketScreenContent(
            hargaTiket = 25000,
            jumlahTiket = 1,
            namaPembeli = "Budi Santoso",
            statusType = StatusType.SUCCESS,
            onNamaChange = {},
            onJumlahIncrement = {},
            onJumlahDecrement = {},
            onPesanClick = {}
        )
    }
}

@Preview(showBackground = true, name = "4. Nama Kosong (Validasi)")
@Composable
fun PreviewNamaKosong() {
    NativeTheme {
        TiketScreenContent(
            hargaTiket = 25000,
            jumlahTiket = 1,
            namaPembeli = "",
            statusType = StatusType.ERROR_EMPTY_NAME,
            onNamaChange = {},
            onJumlahIncrement = {},
            onJumlahDecrement = {},
            onPesanClick = {}
        )
    }
}
