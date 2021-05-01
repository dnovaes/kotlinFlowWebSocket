package com.example.flowwebsocket.features.roomflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.flowwebsocket.R
import com.example.flowwebsocket.databinding.ActivityMainBinding
import com.example.flowwebsocket.extensions.setRowCells
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Integer.parseInt
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initComponents()
    }

    override fun onDestroy() {
        viewModel.closeConnection()
        super.onDestroy()
    }

    private fun initComponents() {
        this.title = getString(R.string.room_flow_screen_title)
        setupScreenLayout { view, rowPos, columnPos ->
            val counter = parseInt(binding.counterIdentifier.text.toString())
            binding.counterIdentifier.text = (counter+1).toString()

            if (viewModel.hasMob(rowPos, columnPos)) {
                viewModel.markMob(rowPos, columnPos, false)
                view.background = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.imageview_border_background
                )
            }
        }
    }

    private fun setupScreenLayout(onClick: (View, Int, Int) -> Unit) {
        binding.helloRoom.text = getString(R.string.title_main_activity)
        binding.row1.setRowCells(this.applicationContext, 0, onClick)
        binding.row2.setRowCells(this.applicationContext, 1, onClick)
        binding.row3.setRowCells(this.applicationContext, 2, onClick)
        binding.row4.setRowCells(this.applicationContext, 3, onClick)
        binding.row5.setRowCells(this.applicationContext, 4, onClick)
        binding.btStartGame.setOnClickListener {
            lifecycleScope.launch {
                viewModel.startGame().collect {
                    binding.btStartGame.visibility = View.GONE
                    viewModel.markMob(it.posX, it.posY)
                    showMob(it.posX, it.posY)
                }
            }
        }
    }

    private fun showMob(posX: Int, posY: Int) {
        val backgroundBlack = ContextCompat.getDrawable(applicationContext, R.mipmap.ic_zubat_foreground)
        when(posX) {
            0 -> binding.row1.getChildAt(posY).background = backgroundBlack
            1 -> binding.row2.getChildAt(posY).background = backgroundBlack
            2 -> binding.row3.getChildAt(posY).background = backgroundBlack
            3 -> binding.row4.getChildAt(posY).background = backgroundBlack
            4 -> binding.row5.getChildAt(posY).background = backgroundBlack
        }
    }
}
