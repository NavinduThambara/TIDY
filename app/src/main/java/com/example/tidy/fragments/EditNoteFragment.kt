package com.example.tidy.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tidy.MainActivity
import com.example.tidy.R
import com.example.tidy.databinding.FragmentEditNoteBinding
import com.example.tidy.model.Task
import com.example.tidy.viewmodel.TaskViewModel

class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var currentTask: Task

    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        // Set up initial view with the current task
        binding.editNoteTitle.setText(currentTask.title)
        binding.editNoteDesc.setText(currentTask.description)

        // Handle save button click
        binding.editNoteFab.setOnClickListener {
            val title = binding.editNoteTitle.text.toString().trim()
            val description = binding.editNoteDesc.text.toString().trim()

            if (title.isNotEmpty()) {
                val task = Task(currentTask.id, title, description)
                taskViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteTask() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Task")
            setMessage("Are you sure you want to delete this task?")
            setPositiveButton("Yes") { _, _ ->
                taskViewModel.deleteNote(currentTask)
                Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("No", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteMenu -> {
                deleteTask()
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding to avoid memory leaks
    }
}
