package com.binar.aplikasicatatanramadhan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView


class RamadhanAdapter(val listRamadhan : List<RamadhanEntity>): RecyclerView.Adapter<RamadhanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recap,parent,false)
        return  ViewHolder(itemView)
    }
    override fun getItemCount(): Int {
       return  listRamadhan.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.itemView.
    }


}