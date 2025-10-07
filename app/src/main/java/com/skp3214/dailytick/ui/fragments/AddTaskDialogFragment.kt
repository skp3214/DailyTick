package com.skp3214.dailytick.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.skp3214.dailytick.models.Task
import com.skp3214.dailytick.databinding.DialogAddTaskBinding
import com.skp3214.dailytick.ui.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddTaskDialogFragment : DialogFragment() {
    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by activityViewModels()
    private var editingTask: Task? = null
    private var selectedDate: Date? = null

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
        editingTask = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ARG_TASK, Task::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(ARG_TASK) as? Task
        }
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

        setupUI()
        setupClickListeners()
        populateFields()
    }

    private fun setupUI() {
        binding.tvDialogTitle.text = if (editingTask != null) "Edit Task" else "Add New Task"

        binding.btnSave.text = if (editingTask != null) "Update" else "Save"
    }

    private fun setupClickListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            saveTask()
        }

        binding.etDueDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun populateFields() {
        editingTask?.let { task ->
            binding.etTaskTitle.setText(task.title)
            binding.etTaskDescription.setText(task.description)

            when (task.priority) {
                "HIGH" -> binding.chipGroupPriority.check(binding.chipHigh.id)
                "MEDIUM" -> binding.chipGroupPriority.check(binding.chipMedium.id)
                "LOW" -> binding.chipGroupPriority.check(binding.chipLow.id)
            }

            task.dueDate?.let { date ->
                selectedDate = date
                val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                binding.etDueDate.setText(formatter.format(date))
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        selectedDate?.let { calendar.time = it }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time

                val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                binding.etDueDate.setText(formatter.format(selectedDate!!))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun saveTask() {
        val title = binding.etTaskTitle.text.toString().trim()

        if (title.isEmpty()) {
            binding.etTaskTitle.error = "Task title is required"
            return
        }

        val description = binding.etTaskDescription.text.toString().trim()
        val safeDescription = if (description.isEmpty()) null else description

        val priority = when (binding.chipGroupPriority.checkedChipId) {
            binding.chipHigh.id -> "HIGH"
            binding.chipMedium.id -> "MEDIUM"
            binding.chipLow.id -> "LOW"
            else -> "LOW"
        }

        if (editingTask != null) {
            val updatedTask = editingTask!!.copy(
                title = title,
                description = safeDescription,
                priority = priority,
                dueDate = selectedDate
            )
            taskViewModel.updateTask(updatedTask)
        } else {
            val newTask = Task(
                id = 0,
                title = title,
                description = safeDescription,
                priority = priority,
                dueDate = selectedDate,
                isCompleted = false,
                createdAt = Date(),
                userEmail = ""
            )
            taskViewModel.addTask(newTask)
        }

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
