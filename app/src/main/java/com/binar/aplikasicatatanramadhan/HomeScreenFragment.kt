package com.binar.aplikasicatatanramadhan

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.aplikasicatatanramadhan.databinding.FragmentEditInputBinding
import com.binar.aplikasicatatanramadhan.databinding.FragmentHomeScreenBinding
import com.binar.aplikasicatatanramadhan.databinding.FragmentInputFormBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeScreenFragment : Fragment() {

    private var mDB: RamadhanDatabase? = null
    private lateinit var preferences: SharedPreferences
    private var _binding : FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = requireContext().getSharedPreferences(LoginFragment.LOGINUSER, Context.MODE_PRIVATE)
        binding.tvNama.text = "${preferences.getString(LoginFragment.USERNAME,null)}"

        mDB = RamadhanDatabase.getInstance(requireContext())
        binding.rvListRamadhan.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fetchData()
        userLogout()

        binding.ibJadwal.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_jadwal_ibadah)
        }

        binding.ibCatatanSatu.setOnClickListener {
            catatan()
        }


        // Menambahkan Data
        binding.ibCatatanDua.setOnClickListener {
            catatan()
        }

    }



    fun fetchData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val listRamadhan = mDB?.ramadhanDao()?.getAllData()

            runBlocking(Dispatchers.Main) {
                listRamadhan?.let {
                    val adapter = RamadhanAdapter(
                        it,
                        details = { RamadhanEntity ->

                            val dialogBinding =
                                FragmentEditInputBinding.inflate(LayoutInflater.from(requireContext()))
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setView(dialogBinding.root)
                            val dialog = dialogBuilder.create()
                            dialog.setCancelable(true)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                            if (RamadhanEntity.berpuasa) {
                                dialogBinding.cbPuasa.isChecked = true
                            }
                            dialogBinding.tiInputHari.setText(RamadhanEntity.hari.toString())//text = "${RamadhanEntity.hari.toString().toInt()}"
                            dialogBinding.tiInputTaggal.setText(RamadhanEntity.tanggal)
                            dialogBinding.etInputCatatan.setText(RamadhanEntity.catatan)

                            if (RamadhanEntity.lima_waktu) {
                                dialogBinding.cbInputSholat.isChecked = true
                            }
                            if (RamadhanEntity.tarawih) {
                                dialogBinding.cbInputTarawih.isChecked = true
                            }
                            if (RamadhanEntity.tahajud) {
                                dialogBinding.cbInputTahajud.isChecked = true
                            }
                            if (RamadhanEntity.quran) {
                                dialogBinding.cbInputQuran.isChecked = true
                            }
                            if (RamadhanEntity.sedekah) {
                                dialogBinding.cbInputSedekah.isChecked = true
                            }
                            if (RamadhanEntity.kajian) {
                                dialogBinding.cbInputKajian.isChecked = true
                            }

                            //Update data
                            dialogBinding.ibUpdate.setOnClickListener {
                                val data = RamadhanEntity(
                                    RamadhanEntity.id,
                                    dialogBinding.cbPuasa.isChecked,
                                    dialogBinding.tiInputHari.text.toString().toInt(),
                                    dialogBinding.tiInputTaggal.text.toString(),
                                    dialogBinding.etInputCatatan.text.toString(),
                                    dialogBinding.cbInputSholat.isChecked,
                                    dialogBinding.cbInputTarawih.isChecked,
                                    dialogBinding.cbInputTahajud.isChecked,
                                    dialogBinding.cbInputQuran.isChecked,
                                    dialogBinding.cbInputSedekah.isChecked,
                                    dialogBinding.cbInputKajian.isChecked,
                                )
                                dialog.setCancelable(true)
                                lifecycleScope.launch(Dispatchers.IO) {
                                    val result = mDB?.ramadhanDao()?.updateRamadhan(data)
                                    runBlocking(Dispatchers.Main) {
                                        if (result != 0) {
                                            Toast.makeText(
                                                requireContext(),
                                                "Berhasil Diubah",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "Gagal Mengubah",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        fetchData()
                                        dialog.dismiss()
                                    }
                                }
                            }

                            dialogBinding.ivHapus.setOnClickListener {
                                AlertDialog.Builder(requireContext())
                                    .setPositiveButton("Ya") { p0, p1 ->
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            val result =
                                                mDB?.ramadhanDao()?.deleteRamadhan(RamadhanEntity)
                                            runBlocking(Dispatchers.Main) {
                                                if (result != 0) {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Berhasil Didelete",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    dialog.dismiss()
                                                } else {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Gagal Menghapus",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                fetchData()
                                            }
                                        }
                                    }.setNegativeButton("Tidak") { hapus, p1 ->
                                        hapus.dismiss()
                                    }
                                    .setMessage("Apakah anda yakin mau hapus hari ke ${RamadhanEntity.hari}?")
                                    .setTitle("Konfirmasi Hapus").create().show()
                            }
                            dialog.show()

                        })
                    binding.rvListRamadhan.adapter = adapter
                }
            }
        }
    }

     private fun catatan() {
        val dialogBinding =  FragmentInputFormBinding.inflate(LayoutInflater.from(requireContext()))

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogBinding.root)

        val dialog = dialogBuilder.create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.ibSimpan.setOnClickListener {
            val data = RamadhanEntity(
                null,
                dialogBinding.cbPuasa.isChecked,
                dialogBinding.tiInputHari.text.toString().toInt(),
                dialogBinding.tiInputTaggal.text.toString(),
                dialogBinding.etInputCatatan.text.toString(),
                dialogBinding.cbInputSholat.isChecked,
                dialogBinding.cbInputTarawih.isChecked,
                dialogBinding.cbInputTahajud.isChecked,
                dialogBinding.cbInputQuran.isChecked,
                dialogBinding.cbInputSedekah.isChecked,
                dialogBinding.cbInputKajian.isChecked
            )
            lifecycleScope.launch(Dispatchers.IO) {
                val result = mDB?.ramadhanDao()?.insertRamadhan(data)
                runBlocking(Dispatchers.Main) {
                    if (result != 0.toLong()) {
                        Toast.makeText(
                            requireContext(),
                            "Berhasil Ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Gagal Menambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    fetchData()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }


    private fun userLogout() {
        binding.ibLogout.setOnClickListener {
            val logoutDialog = AlertDialog.Builder(requireContext())
            logoutDialog.setTitle("Logout")
            logoutDialog.setMessage("Apakah anda yakin ingin logout?")
            logoutDialog.setPositiveButton("Logout") {p0, _ ->
                p0.dismiss()
                preferences.edit().clear().apply()
                findNavController().navigate(R.id.action_homeScreenFragment_to_loginFragment)
            }
            logoutDialog.setNegativeButton("Batal") {p0, _ ->
                p0.dismiss()
            }
            logoutDialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RamadhanDatabase.destroyInstance()
    }
}

