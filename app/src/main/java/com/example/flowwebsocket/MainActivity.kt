package com.example.flowwebsocket

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Formatter
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.flowwebsocket.databinding.ActivityMainBinding
import com.example.flowwebsocket.socket.log
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
    }

    override fun onResume() {
        super.onResume()

        val wifiMan: WifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        log("ipd ${Formatter.formatIpAddress(wifiMan.connectionInfo.ipAddress)}")
    }

    override fun onDestroy() {
        viewModel.closeConnection()
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun initComponents() {
        lifecycleScope.launch {
            viewModel.mockedRealTimePositions().collect { roomData ->
                binding.helloRoom.text = "${roomData.name}: posX: ${roomData.posX}, posY: ${roomData.posY}"
                viewModel.move(posX = 90, posY = 90)
            }
            viewModel.listenMovements().collect {
                binding.helloRoom.text = "Receiving movement at: ${it.posX}, ${it.posY}"
            }
        }
    }

}
