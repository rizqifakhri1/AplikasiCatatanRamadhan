package com.binar.aplikasicatatanramadhan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.binar.aplikasicatatanramadhan.databinding.FragmentEditInputBinding
import com.binar.aplikasicatatanramadhan.databinding.FragmentRegisterBinding
import kotlinx.coroutines.*

class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var mDb : RamadhanDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDb = RamadhanDatabase.getInstance(requireContext())


        binding.btnRegister.setOnClickListener{
            when {
                binding.tiUserNameEditText.text.isNullOrEmpty() -> {
                    binding.tiUserNameLayout.error = "Username belum diisi"
                    Toast.makeText(requireContext(), "Username belum diisi", Toast.LENGTH_SHORT).show()
                }
                binding.tiUserEmailEditText.text.isNullOrEmpty() -> {
                    binding.tiUserEmailLayout.error = "Email belum diisi"
                    Toast.makeText(requireContext(), "Email belum diisi", Toast.LENGTH_SHORT).show()
                }
                binding.tiUserPasswordEditText.text.isNullOrEmpty() -> {
                    binding.tiUserPasswordLayout.error = "Password belum diisi"
                    Toast.makeText(requireContext(), "Password belum diisi", Toast.LENGTH_SHORT).show()
                }
                binding.tiUserPasswordConfirmEditText.text.isNullOrEmpty() -> {
                    binding.tiUserPasswordConfirmLayout.error = "Password Confirm belum diisi"
                    Toast.makeText(requireContext(), "Password Confirm belum diisi", Toast.LENGTH_SHORT).show()
                }
                binding.tiUserPasswordEditText.text.toString().lowercase() != binding.tiUserPasswordConfirmEditText.text.toString().lowercase() -> {
                    binding.tiUserPasswordConfirmLayout.error = "Password tidak sama, harap ulangi kembali"
                    Toast.makeText(requireContext(), "Password tidak sama, harap ulangi kembali", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val user = UserEntity(
                        null,
                        binding.tiUserNameEditText.text.toString(),
                        binding.tiUserEmailEditText.text.toString(),
                        binding.tiUserPasswordEditText.text.toString()
                    )
                    lifecycleScope.launch(Dispatchers.IO) {
                    val result = mDb?.userDao()?.insertUser(user)
                        runBlocking(Dispatchers.Main){
                        if (result != 0.toLong()){
                            Toast.makeText(activity, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "Registrasi Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }

            }
        }

        // Sudah Punya Akun
        binding.tvLoginText.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}