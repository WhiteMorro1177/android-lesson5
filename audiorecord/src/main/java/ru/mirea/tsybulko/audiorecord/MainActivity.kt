package ru.mirea.tsybulko.audiorecord

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import ru.mirea.tsybulko.audiorecord.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val recorder: MediaRecorder = MediaRecorder()
    private val player: MediaPlayer = MediaPlayer()

    private lateinit var recordFilePath: String
    private var isRecording = false
    private var isPlaying = false

    companion object {
        const val TAG = "MainActivity"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recordButton = binding.recordButton
        val playButton = binding.playButton
        playButton.isEnabled = false
        recordFilePath = File(
            getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "/audiorecordtest.3gp"
        ).absolutePath

        // check permissions
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO
            ) == PermissionChecker.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PermissionChecker.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 200
            )
        }

        // record button click
        recordButton.setOnClickListener {
            if (isRecording) {
                recordButton.text = "Start recording"
                playButton.isEnabled = true
                stopRecording()
            } else {
                recordButton.text = "Stop recording"
                playButton.isEnabled = false
                startRecording()
            }
            isRecording = !isRecording
        }

        // play button click
        playButton.setOnClickListener {
            if (isPlaying) {
                playButton.text = "Stop playing"
                playButton.isEnabled = false
                startPlaying()
            }
            else {
                playButton.text = "Start playing"
                playButton.isEnabled = true
                stopPlaying()
            }
            isPlaying = !isPlaying
        }
    }

    private fun startRecording() {
        recorder.run {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(recordFilePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        }
        try {
            recorder.prepare()
        } catch (e: IOException) {
            Log.e(TAG, "prepare() failed")
        }
        recorder.start()
    }

    private fun stopRecording() {
        recorder.run {
            stop()
            release()
            reset()
        }
    }

    private fun startPlaying() {
        try {
            player.run {
                setDataSource(recordFilePath)
                prepare()
                start()
            }
        } catch (e: IOException) {
            Log.e(TAG, "prepare() failed")
        }
    }

    private fun stopPlaying() {
        player.run {
            release()
            reset()
        }
    }
}