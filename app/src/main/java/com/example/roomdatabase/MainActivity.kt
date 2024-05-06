package com.example.roomdatabase

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.roomdatabase.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentDatabase: StudentDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        studentDatabase = StudentDatabase.getDatabase(this)

        binding.saveBtn.setOnClickListener {
            saveData()
        }
        binding.searchBtn.setOnClickListener {
            searchData()
        }
        binding.deleteAllBtn.setOnClickListener {
            GlobalScope.launch {
                studentDatabase.StudentDao().deleteAll()
            }
        }

    }

    private fun saveData() {
        val firstName = binding.firstNameEt.text.toString()
        val lastName = binding.lastNameEt.text.toString()
        val rollNo = binding.rollNoEt.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            val student = Student(0, firstName, lastName, rollNo.toInt())
            GlobalScope.launch {
                studentDatabase.StudentDao().insert(student)
            }
            binding.firstNameEt.text?.clear()
            binding.lastNameEt.text?.clear()
            binding.rollNoEt.text?.clear()
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchData() {
        val rollNo = binding.rollNoEt.text.toString()
        if (rollNo.isNotEmpty()) {
            lateinit var student: Student
            GlobalScope.launch {
                student = studentDatabase.StudentDao().findById(rollNo.toInt())
                if (studentDatabase.StudentDao().isEmpty()) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@MainActivity,"No data find", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    displayData(student)
                }
            }
        }
    }

    private suspend fun displayData(student: Student) {
        withContext(Dispatchers.Main) {
            binding.firstNameEt.setText(student.firstName.toString())
            binding.lastNameEt.setText(student.lastName.toString())
            binding.rollNoEt.setText(student.rollno.toString())

        }
    }
}

