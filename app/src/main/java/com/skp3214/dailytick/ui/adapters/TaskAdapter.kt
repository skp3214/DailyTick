package com.skp3214.dailytick.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skp3214.dailytick.databinding.ItemTasksBinding
import com.skp3214.dailytick.models.Task

class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskClick: (Task) -> Unit,
    private val onTaskLongClick: (Task) -> Unit,
    private val onCompleteClick: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(private val binding: ItemTasksBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            task: Task,
            onTaskClick: (Task) -> Unit,
            onTaskLongClick: (Task) -> Unit,
            onCompleteClick: (Task) -> Unit,
            onDeleteClick: (Task) -> Unit
        ) {
            binding.tvTaskTitle.text = task.title
            binding.tvTaskDesc.text = task.description
            binding.tvDue.text = "Due: ${task.dueDate}"
            binding.tvPriority.text = task.priority

            // Set completion status
            binding.cbCompleted.isChecked = task.isCompleted

            // Set click listeners
            binding.root.setOnClickListener { onTaskClick(task) }
            binding.root.setOnLongClickListener {
                onTaskLongClick(task)
                true
            }
            binding.cbCompleted.setOnClickListener { onCompleteClick(task) }
            binding.btnDelete.setOnClickListener { onDeleteClick(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position], onTaskClick, onTaskLongClick, onCompleteClick, onDeleteClick)
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}