package com.example.roomdatabase

import android.content.Intent
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
        binding.showBtn.setOnClickListener {
            startActivity(Intent(this, ShowData::class.java))
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
        val rollNo = binding.SearchRollNoEt.text.toString()
        if (rollNo.isNotEmpty()) {

            GlobalScope.launch {
               var student:Student = studentDatabase.StudentDao().findById(rollNo.toInt())
                if (studentDatabase.StudentDao().isEmpty()) {
                    Handler(Looper.getMainLooper()).post{
                        Toast.makeText(this@MainActivity, "No data find", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    displayData(student)
                }
            }
        }
    }

    private suspend fun displayData(student: Student) {
        withContext(Dispatchers.Main) {
            val fname = student?.firstName.toString()
            val lname = student?.lastName.toString()
            val roll = student?.rollno.toString()

            if (fname.isNotEmpty() || lname.isNotEmpty() || roll.isNotEmpty()) {
                binding.firstNameEt.setText("")
                binding.lastNameEt.setText("")
                binding.rollNoEt.setText("")
                Toast.makeText(this@MainActivity, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                binding.firstNameEt.setText(fname)
                binding.lastNameEt.setText(lname)
                binding.rollNoEt.setText(roll)

            }
        }
    }
}

