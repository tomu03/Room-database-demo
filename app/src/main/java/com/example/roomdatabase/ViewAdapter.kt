package com.example.roomdatabase



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.databinding.ListCardBinding

class ViewAdapter(private val dataList:List<Student>):RecyclerView.Adapter<ViewAdapter.myViewHolder>() {
    class myViewHolder(val binding: ListCardBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(ListCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val allData = dataList[position]
        holder.binding.apply {
            fNameText.text = allData.firstName
            lNameText.text = allData.lastName
            rollText.text = allData.rollno.toString()
        }
    }
}