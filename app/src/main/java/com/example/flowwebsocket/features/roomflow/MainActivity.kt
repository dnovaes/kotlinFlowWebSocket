package com.example.flowwebsocket.features.roomflow

import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Formatter
import androidx.lifecycle.lifecycleScope
import com.example.flowwebsocket.R
import com.example.flowwebsocket.databinding.ActivityMainBinding
import com.example.flowwebsocket.extensions.setRowCells
import com.example.flowwebsocket.socket.log
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Integer.parseInt
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    //private val viewModel by viewModels<MainViewModel>()
    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
        setEvents()
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

    private fun initComponents() {
        this.title = getString(R.string.room_screen_title)
        setupScreenLayout {
            val counter = parseInt(binding.counterIdentifier.text.toString())
            binding.counterIdentifier.text = (counter+1).toString()
        }
    }

    private fun setupScreenLayout(onClick: () -> Unit) {
        binding.helloRoom.text = getString(R.string.title_main_activity)
        binding.row1.setRowCells(this.applicationContext, onClick)
        binding.row2.setRowCells(this.applicationContext, onClick)
        binding.row3.setRowCells(this.applicationContext, onClick)
        binding.row4.setRowCells(this.applicationContext, onClick)
        binding.row5.setRowCells(this.applicationContext, onClick)
    }

    private fun setEvents() {
        lifecycleScope.launch {
            viewModel.mockedRealTimePositions().collect { roomData ->
                binding.helloRoom.text = "${roomData.name}: posX: ${roomData.posX}, posY: ${roomData.posY}"
                viewModel.move(posX = 90, posY = 90)
            }
            viewModel.onMovements().collect {
                binding.helloRoom.text = "Receiving movement at: ${it.posX}, ${it.posY}"
            }
        }
    }

}
