package com.skp3214.dailytick.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skp3214.dailytick.models.Task
import com.skp3214.dailytick.databinding.ItemTasksBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onCompleteClick: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTasksBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
        private val binding: ItemTasksBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                tvTaskTitle.text = task.title

                if (!task.description.isNullOrEmpty()) {
                    tvTaskDescription.text = task.description
                    tvTaskDescription.visibility = View.VISIBLE
                } else {
                    tvTaskDescription.visibility = View.GONE
                }

                cbTaskComplete.isChecked = task.isCompleted

                if (task.isCompleted) {
                    tvTaskTitle.paintFlags = tvTaskTitle.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                    tvTaskTitle.alpha = 0.6f
                    tvTaskDescription.alpha = 0.4f
                } else {
                    tvTaskTitle.paintFlags = tvTaskTitle.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    tvTaskTitle.alpha = 1.0f
                    tvTaskDescription.alpha = 0.7f
                }

                chipPriority.text = when (task.priority) {
                    "HIGH" -> "High"
                    "MEDIUM" -> "Medium"
                    "LOW" -> "Low"
                    else -> "Low"
                }

                task.dueDate?.let { date ->
                    val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
                    tvDueDate.text = formatter.format(date)
                    tvDueDate.visibility = View.VISIBLE
                } ?: run {
                    tvDueDate.visibility = View.GONE
                }

                root.setOnClickListener { onTaskClick(task) }

                cbTaskComplete.setOnClickListener { onCompleteClick(task) }
                btnEdit.setOnClickListener { onTaskClick(task) }
                btnDelete.setOnClickListener { onDeleteClick(task) }
            }
        }
    }

    fun updateTasks(newTasks: List<Task>) {
        submitList(newTasks)
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}