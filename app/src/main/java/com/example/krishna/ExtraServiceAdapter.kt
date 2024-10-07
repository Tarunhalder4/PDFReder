package com.example.krishna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.krishna.dao.ServiceEntity
import com.example.krishna.model.ExtraService


class ExtraServiceAdapter(private val mainActivity: MainActivity, private val itemList:List<ExtraService>) : RecyclerView.Adapter<ExtraServiceAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.extra_service_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var quantity = 0

        val currentItem = itemList[position]

        holder.checkBox.text = currentItem.addon_service_id
        holder.price.text = "₹ "+currentItem.addon_service_price
        holder.checkBox.isChecked = checkBoxCheck(currentItem)

        holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                quantity = parent.getItemAtPosition(position).toString().toInt()
                mainActivity.updateData(quantity,currentItem.as_id)

                Thread.sleep(100)
                mainActivity.shopViewModel.getDataFromDataBase()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(holder.checkBox.isChecked){
                val service = ServiceEntity(currentItem.as_id,currentItem.addon_service_id,currentItem.addon_service_price,quantity, "10")
                mainActivity.insertData(service)
            }else{
                mainActivity.deleteData(currentItem.as_id)
            }

            Thread.sleep(100)
            mainActivity.shopViewModel.getDataFromDataBase()
        }


    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val checkBox: CheckBox = view.findViewById(R.id.checkbox)
        val price: TextView = view.findViewById(R.id.price)
        val spinner: Spinner = view.findViewById(R.id.spinner)

    }

    private fun checkBoxCheck(currentItem:ExtraService): Boolean {
        val list = mainActivity.getAllDataFromDataBase()
        list.forEach {
            if (it.id == currentItem.as_id) {
                return true
            }
        }
        return false
    }


}