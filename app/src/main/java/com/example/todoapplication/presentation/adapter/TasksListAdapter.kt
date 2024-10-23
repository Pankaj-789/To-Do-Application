package com.example.todoapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplication.data.Tasks
import com.example.todoapplication.databinding.EachitemsRvBinding

class TasksListAdapter(private val onDeleteClick : (Tasks) -> Unit,
    private val onEditClick : (Tasks) -> Unit) :
    ListAdapter<Tasks, TasksListAdapter.TaskViewHolder>(DiffCallBack()) {

    class TaskViewHolder(private val binding : EachitemsRvBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task : Tasks,onDeleteClick: (Tasks) -> Unit,onEditClick: (Tasks) -> Unit){
            binding.headingTv.text = task.title
            binding.subHeadingTv.text = task.description
            binding.deleteIv.setOnClickListener{
                onDeleteClick.invoke(task)
            }
            binding.editIv.setOnClickListener{
                onEditClick(task)

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksListAdapter.TaskViewHolder {
        val binding = EachitemsRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)


    }

    override fun onBindViewHolder(holder: TasksListAdapter.TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, onDeleteClick,onEditClick)

    }

    class DiffCallBack: DiffUtil.ItemCallback<Tasks>() {
        override fun areItemsTheSame(oldItem: Tasks, newItem: Tasks): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Tasks, newItem: Tasks): Boolean {
            return oldItem == newItem

        }

    }

}

