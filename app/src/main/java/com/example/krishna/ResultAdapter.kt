package com.example.krishna

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.krishna.dao.ServiceEntity

class ResultAdapter(private val itemList:List<ServiceEntity>): RecyclerView.Adapter<ResultAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.quantityAndService.text  = "${currentItem.quantity} X  ${currentItem.servicePrice} ${currentItem.serviceName}"
        holder.service.text  = currentItem.serviceName

        if(currentItem.discountPercentage.isNotEmpty()){
            //holder.checkBox.visibility = View.GONE
            holder.delivaryTime.visibility = View.VISIBLE
            holder.actualPrice.paintFlags  = holder.actualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.actualPrice.text  = (currentItem.quantity*currentItem.servicePrice.toFloat()).toString()
            holder.discountPrice.text  = "₹ " + discountedPrice(currentItem)
        }else{
           // holder.checkBox.visibility = View.VISIBLE
            holder.delivaryTime.visibility = View.GONE
            holder.actualPrice.text = ""
            holder.discountPrice.text  = "₹ " +(currentItem.quantity*currentItem.servicePrice.toFloat()).toString()
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quantityAndService: TextView = view.findViewById(R.id.quantity_and_service)
        val service: TextView = view.findViewById(R.id.service)
        val actualPrice: TextView = view.findViewById(R.id.actual_price)
        val discountPrice:TextView = view.findViewById(R.id.discount_price)
        val delivaryTime:TextView = view.findViewById(R.id.deliveryTime)
        val checkBox:CheckBox = view.findViewById(R.id.checkBoxUrgent)
    }

    private fun discountedPrice(serviceEntity: ServiceEntity):Float{
        val actualPercentage = 100 - 10
        return (serviceEntity.quantity*serviceEntity.servicePrice.toFloat()*actualPercentage)/100
    }

}