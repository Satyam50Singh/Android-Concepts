package com.rivest.practiceapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rivest.practiceapp.R

class FragmentA : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("lifecycle fragment A", "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("lifecycle fragment A", "onCreate")

        Toast.makeText(requireContext(), "Fragment A attached successfully!", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.e("lifecycle fragment A", "onCreateView")
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    override fun onStart() {
        super.onStart()
        Log.e("lifecycle fragment A", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("lifecycle fragment A", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("lifecycle fragment A", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("lifecycle fragment A", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("lifecycle fragment A", "onDestroyView")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("lifecycle fragment A", "onViewCreated")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("lifecycle fragment A", "onDetach")
    }
}