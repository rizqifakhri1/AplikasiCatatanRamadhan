package com.binar.aplikasicatatanramadhan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.binar.aplikasicatatanramadhan.databinding.FragmentJadwalIbadahBinding

class jadwal_ibadah : Fragment() {

    private lateinit var binding:FragmentJadwalIbadahBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJadwalIbadahBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding
        binding.ibBack.setOnClickListener(){
            findNavController().navigate(R.id.action_jadwal_ibadah_to_homeScreenFragment)
        }
    }

}