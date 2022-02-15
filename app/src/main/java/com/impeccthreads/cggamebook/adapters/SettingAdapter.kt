package com.impeccthreads.cggamebook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cggamebook.R
import com.impeccthreads.cggamebook.utility.listen

class SettingAdapter(val mContext: Context, var modules: ArrayList<String>, val delegate: SettingAdapterListener?): RecyclerView.Adapter<SettingAdapter.SettingViewHolder>() {

    interface SettingAdapterListener {

        fun didSelectViewAtPosition(position: Int)
    }

    override fun getItemCount(): Int {
        return modules.size
    }

    fun getModuleList(): List<String> {
        return modules
    }

    fun setModuleList(tables: ArrayList<String>) {
        this.modules = tables
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_settings_list, parent, false)
        val viewHolder = SettingViewHolder(cellForRow).listen { position, type ->
            delegate?.didSelectViewAtPosition(position)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {

        val moduleName = modules.get(position)

        holder.textViewTableName.text = moduleName.capitalize()
    }


    class SettingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val textViewTableName = view.findViewById<TextView>(R.id.textViewTableName) as TextView

    }
}