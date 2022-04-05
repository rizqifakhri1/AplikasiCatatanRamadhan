package com.binar.aplikasicatatanramadhan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.binar.aplikasicatatanramadhan.databinding.FragmentHomeScreenBinding

class HomeScreenFragment : Fragment() {

    private  lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibJadwal.setOnClickListener{
            findNavController().navigate(R.id.action_homeScreenFragment_to_jadwal_ibadah)
        }

        binding.ibTambahkanMenu.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_inputFormFragment)
        }
    }
}