package com.binar.aplikasicatatanramadhan

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.binar.aplikasicatatanramadhan.databinding.ItemRecapBinding


class RamadhanAdapter(val listRamadhan : List<RamadhanEntity>, val details :(RamadhanEntity)-> Unit): RecyclerView.Adapter<RamadhanAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRecapBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ViewHolder(binding)
    }
    override fun getItemCount(): Int {
       return  listRamadhan.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvInputPuasaKe.text = listRamadhan[position].hari.toString()
            tvInputStarPuasaKe.text = listRamadhan[position].hari.toString()
            tvInputTglPuasa.text = listRamadhan[position].tanggal
            ivEdit.setOnClickListener{
                details.invoke(listRamadhan[position])
            }
            }
        }
}