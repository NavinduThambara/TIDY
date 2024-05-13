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
import java.util.Calendar


class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {

    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var addTaskView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
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
        val description = binding.addNoteDesc.text.toString().trim()
        val date = getDateOnly()
        val time = getTimeOnly()

        if (title.isNotEmpty()){
            val task = Task(0, title, description, date , time  )
            taskViewModel.addTask(task)

            Toast.makeText(addTaskView.context, "Task added successfully", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }else{
            Toast.makeText(addTaskView.context, "Please enter a title", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTimeOnly(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour

        val calendar = Calendar.getInstance()
        // Set other fields to current values to avoid exceptions
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
        // Set time
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        return calendar.timeInMillis
    }


    private fun getDateOnly(): Long {
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.timeInMillis
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

    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null
    }
}