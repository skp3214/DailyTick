package com.skp3214.dailytick.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.skp3214.dailytick.databinding.FragmentHomeBinding
import com.skp3214.dailytick.ui.adapters.TaskAdapter
import com.skp3214.dailytick.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by activityViewModels()

    private lateinit var pendingTasksAdapter: TaskAdapter
    private lateinit var completedTasksAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupFAB()
        observeTasks()
    }

    private fun setupRecyclerViews() {
        pendingTasksAdapter = TaskAdapter(
            tasks = emptyList(),
            onTaskClick = { task ->
                val dialog = AddTaskDialogFragment.newInstance(task)
                dialog.show(parentFragmentManager, "EditTaskDialog")
            },
            onTaskLongClick = { task ->
            },
            onCompleteClick = { task ->
                taskViewModel.toggleTaskCompletion(task)
            },
            onDeleteClick = { task ->
                taskViewModel.deleteTask(task)
            }
        )

        completedTasksAdapter = TaskAdapter(
            tasks = emptyList(),
            onTaskClick = { task ->
                // Edit task on click
                val dialog = AddTaskDialogFragment.newInstance(task)
                dialog.show(parentFragmentManager, "EditTaskDialog")
            },
            onTaskLongClick = { task ->
            },
            onCompleteClick = { task ->
                taskViewModel.toggleTaskCompletion(task)
            },
            onDeleteClick = { task ->
                taskViewModel.deleteTask(task)
            }
        )

        binding.rvPendingTasks.apply {
            adapter = pendingTasksAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.rvCompletedTasks.apply {
            adapter = completedTasksAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupFAB() {
        binding.fabAddTask.setOnClickListener {
            val dialog = AddTaskDialogFragment.newInstance()
            dialog.show(parentFragmentManager, "AddTaskDialog")
        }
    }

    private fun observeTasks() {
        viewLifecycleOwner.lifecycleScope.launch {
            taskViewModel.pendingTasks.collect { tasks ->
                pendingTasksAdapter.updateTasks(tasks)

                if (tasks.isEmpty()) {
                    binding.rvPendingTasks.visibility = View.GONE
                    binding.tvNoPendingTasks.visibility = View.VISIBLE
                } else {
                    binding.rvPendingTasks.visibility = View.VISIBLE
                    binding.tvNoPendingTasks.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            taskViewModel.completedTasks.collect { tasks ->
                completedTasksAdapter.updateTasks(tasks)

                if (tasks.isEmpty()) {
                    binding.rvCompletedTasks.visibility = View.GONE
                    binding.tvNoCompletedTasks.visibility = View.VISIBLE
                } else {
                    binding.rvCompletedTasks.visibility = View.VISIBLE
                    binding.tvNoCompletedTasks.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}