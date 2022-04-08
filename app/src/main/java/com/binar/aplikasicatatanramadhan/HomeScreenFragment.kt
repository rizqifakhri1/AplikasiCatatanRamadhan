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

    //Inisiasi Variabel yang dibutuhkan
    private var mDB: RamadhanDatabase? = null
    private lateinit var preferences: SharedPreferences
    private var _binding : FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment - Inisiasi Binding
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Membuat fun onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Implementasi Save Preferences
        preferences = requireContext().getSharedPreferences(LoginFragment.LOGINUSER, Context.MODE_PRIVATE)
        //Untuk menyapa user Assalamualaikum ${username}
        binding.tvNama.text = "${preferences.getString(LoginFragment.USERNAME,null)}"

        mDB = RamadhanDatabase.getInstance(requireContext())
        //Layout Manager buat Recyclerview
        binding.rvListRamadhan.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fetchData()
        userLogout()

       // Untuk mengarahkan ke fragment screen jadwal ibadah (Tidak membawa data)
        binding.ibJadwal.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_jadwal_ibadah)
        }

        // Untuk menambah catatan (tombol atas)
        binding.ibCatatanSatu.setOnClickListener {
            catatan()
        }

        // Untuk menambahkan catatan (tombol bawah)
        binding.ibCatatanDua.setOnClickListener {
            catatan()
        }
        // Untuk menambahkan catatan apabila rv kosong (ada gambar visibel yang bisa di click)
        binding.ivKosong.setOnClickListener {
            catatan()
        }

    }

    // Codingan untuk input data
    fun fetchData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val listRamadhan = mDB?.ramadhanDao()?.getAllData()

            runBlocking(Dispatchers.Main) {
                listRamadhan?.let {

                    // Menampilkan gambar apabila recyclerview kosong
                    if (RamadhanAdapter(it, {}).itemCount ==0) {
                        binding.ivKosong.visibility = View.VISIBLE
                    } else {
                        binding.ivKosong.visibility = View.GONE
                    }

                    //Dialog Input
                    val adapter = RamadhanAdapter(
                        it,
                        details = { RamadhanEntity ->
                            val dialogBinding = FragmentEditInputBinding.inflate(LayoutInflater.from(requireContext()))
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setView(dialogBinding.root)
                            val dialog = dialogBuilder.create()
                            
                            dialog.setCancelable(true) //Untuk bisa di cancle
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //Untuk membuat background transparant

                            //Mulai membaca input dari dailog fragment
                            if (RamadhanEntity.berpuasa) {
                                dialogBinding.cbPuasa.isChecked = true
                            }
                            dialogBinding.tiInputHari.setText(RamadhanEntity.hari.toString())
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

