package com.example.flowwebsocket.features.roomflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.flowwebsocket.R
import com.example.flowwebsocket.databinding.ActivityMainBinding
import com.example.flowwebsocket.extensions.setRowCells
import com.example.flowwebsocket.features.roomflow.model.MobDangerState
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
        setupEvents()
    }

    override fun onDestroy() {
        viewModel.closeConnection()
        super.onDestroy()
    }

    private fun initComponents() {
        this.title = getString(R.string.room_flow_screen_title)

        setupScreenLayout(onClickCell = { view, rowPos, columnPos ->
            if (viewModel.hasMob(rowPos, columnPos)) {
                updateMobCounter()
                viewModel.updateMobCache(rowPos, columnPos, false)
                viewModel.removeMob(rowPos, columnPos)
                view.background = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.imageview_border_background
                )
            }
        })
    }

    private fun setupEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.dangerStateFlow.collect {
                if (it == MobDangerState.Peacefull) {
                    binding.textviewDangerstateDescription.visibility = View.INVISIBLE
                } else {
                    binding.textviewDangerstateDescription.visibility = View.VISIBLE
                }
                println("logd dangerState: $it")
                binding.textviewDangerstateDescription.text = it.description
            }
        }

        lifecycleScope.launch {
            viewModel.onStartGame().collect {
                binding.btStartGame.visibility = View.GONE
                binding.textviewDangerstateDescription.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.onMobChange().collect {
                viewModel.updateMobCache(it.posX, it.posY, it.isNew)
                if (it.isNew) {
                    updateCell(it.posX, it.posY, R.mipmap.ic_zubat_foreground)
                } else {
                    updateCell(it.posX, it.posY, R.drawable.imageview_border_background)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.countdownEmitter().collect {
                binding.textviewTimer.text = "Timer: $it"
            }
        }

        lifecycleScope.launch {
            viewModel.onGameOverFlow.collect {
                if (it)  {
                    binding.imageviewGameoverBackground.visibility = View.VISIBLE
                    binding.imageviewGameoverForeground.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupScreenLayout(onClickCell: (View, Int, Int) -> Unit) {
        binding.helloRoom.text = getString(R.string.title_main_activity)
        binding.row1.setRowCells(this.applicationContext, 0, onClickCell)
        binding.row2.setRowCells(this.applicationContext, 1, onClickCell)
        binding.row3.setRowCells(this.applicationContext, 2, onClickCell)
        binding.row4.setRowCells(this.applicationContext, 3, onClickCell)
        binding.row5.setRowCells(this.applicationContext, 4, onClickCell)
        binding.btStartGame.setOnClickListener {
            viewModel.startGame()
        }
    }

    private fun updateMobCounter() {
        val counter = parseInt(binding.counterIdentifier.text.toString())
        binding.counterIdentifier.text = (counter+1).toString()
    }

    private fun updateCell(posX: Int, posY: Int, backgroundResource: Int) {
        val background = ContextCompat.getDrawable(applicationContext, backgroundResource)
        when(posX) {
            0 -> binding.row1.getChildAt(posY).background = background
            1 -> binding.row2.getChildAt(posY).background = background
            2 -> binding.row3.getChildAt(posY).background = background
            3 -> binding.row4.getChildAt(posY).background = background
            4 -> binding.row5.getChildAt(posY).background = background
        }
    }
}
