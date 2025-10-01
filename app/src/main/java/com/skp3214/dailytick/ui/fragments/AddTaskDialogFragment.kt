package com.skp3214.dailytick.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.skp3214.dailytick.R
import com.skp3214.dailytick.databinding.DialogAddTaskBinding
import com.skp3214.dailytick.models.Task
import com.skp3214.dailytick.ui.viewmodel.TaskViewModel
import java.util.Calendar

class AddTaskDialogFragment : DialogFragment() {
    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by activityViewModels()
    private var existingTask: Task? = null

    companion object {
        private const val ARG_TASK = "task"

        fun newInstance(task: Task? = null): AddTaskDialogFragment {
            val fragment = AddTaskDialogFragment()
            val args = Bundle()
            task?.let { args.putSerializable(ARG_TASK, it) }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_DailyTick)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPrioritySpinner()
        setupDatePicker()

        // Check if editing existing task
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ARG_TASK, Task::class.java)?.let { task ->
                existingTask = task
                populateFields(task)
                binding.tvDialogTitle.text = "Edit Task"
                binding.btnSave.text = "Update"
            }
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(ARG_TASK)?.let { task ->
                existingTask = task as Task
                populateFields(task)
                binding.tvDialogTitle.text = "Edit Task"
                binding.btnSave.text = "Update"
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            saveTask()
        }
    }

    private fun setupPrioritySpinner() {
        val priorities = arrayOf("High", "Medium", "Low")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, priorities)
        binding.spinnerPriority.setAdapter(adapter)
        binding.spinnerPriority.setText("Medium", false)
    }

    private fun setupDatePicker() {
        binding.etDueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                    binding.etDueDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    private fun populateFields(task: Task) {
        binding.etTaskTitle.setText(task.title)
        binding.etTaskDescription.setText(task.description)
        binding.etDueDate.setText(task.dueDate)
        binding.spinnerPriority.setText(task.priority, false)
    }

    private fun saveTask() {
        val title = binding.etTaskTitle.text.toString().trim()
        val description = binding.etTaskDescription.text.toString().trim()
        val dueDate = binding.etDueDate.text.toString().trim()
        val priority = binding.spinnerPriority.text.toString()

        if (title.isEmpty()) {
            binding.etTaskTitle.error = "Title is required"
            return
        }

        if (dueDate.isEmpty()) {
            binding.etDueDate.error = "Due date is required"
            return
        }

        val task = if (existingTask != null) {
            existingTask!!.copy(
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority
            )
        } else {
            Task(
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority
            )
        }

        if (existingTask != null) {
            taskViewModel.updateTask(task)
            Toast.makeText(context, "Task updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            taskViewModel.insertTask(task)
            Toast.makeText(context, "Task added successfully", Toast.LENGTH_SHORT).show()
        }

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
