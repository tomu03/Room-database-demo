package com.example.roomdatabase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabase.databinding.ActivityShowDataBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowData : AppCompatActivity() {
   lateinit var binding: ActivityShowDataBinding
   lateinit var studentDB: StudentDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        studentDB = StudentDatabase.getDatabase(this)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            val studentList = studentDB.StudentDao().getAll()
            val allListShowData = studentList.map{
                Student(id = 0, firstName = it.firstName, lastName = it.lastName, rollno = it.rollno.toInt())
            }
            val adapter =ViewAdapter(allListShowData)
            binding.recycleView.adapter = adapter
        }
    }

}