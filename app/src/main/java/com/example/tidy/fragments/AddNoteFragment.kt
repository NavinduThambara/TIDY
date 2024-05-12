package com.example.tidy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.tidy.MainActivity
import com.example.tidy.R
import com.example.tidy.databinding.FragmentAddNoteBinding
import com.example.tidy.model.Task
import com.example.tidy.viewmodel.TaskViewModel


class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {

    private var addItemBinding: FragmentAddNoteBinding? = null
    private val binding get() = addItemBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var addTaskView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addItemBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTaskView = view
    }

    private fun saveTask(view: View){
        val title = binding.addNoteTitle.text.toString().trim()
        val description = binding.addNoteDesc.toString().trim()

        if (title.isNotEmpty()){
            val task = Task(0, title, description)
            taskViewModel.addTask(task)

            Toast.makeText(addTaskView.context, "Task added successfully", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }else{
            Toast.makeText(addTaskView.context, "Please enter a title", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu -> {
                saveTask(addTaskView)
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addItemBinding = null
    }
}