package com.example.krishna

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.krishna.dao.ServiceEntity
import com.example.krishna.model.ExtraService
import com.example.krishna.model.Service

class ServiceAdapter(private val mainActivity: MainActivity, private val itemList:List<Service>): RecyclerView.Adapter<ServiceAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_layout, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var cont = 0
        val currentItem = itemList[position]

        holder.addBtn.setOnClickListener {
            cont++
            holder.quantity.text = cont.toString()
            if(cont == 1){
                mainActivity.insertData(ServiceEntity(currentItem.item_id,currentItem.item_name,currentItem.price,holder.quantity.text.toString().toInt(),""))
            }else{
                mainActivity.updateData(holder.quantity.text.toString().toInt(),currentItem.item_id)
            }
            Thread.sleep(100)
            mainActivity.shopViewModel.getDataFromDataBase()
        }

        holder.minusBtn.setOnClickListener {
            if(cont>0){
                cont--
                holder.quantity.text = cont.toString()

                if(cont >= 1){
                    mainActivity.updateData(holder.quantity.text.toString().toInt(),currentItem.item_id)
                }else{
                    mainActivity.deleteData(currentItem.item_id)
                }
            }

            Thread.sleep(100)
            mainActivity.shopViewModel.getDataFromDataBase()
        }

        holder.tvServiceName.text = currentItem.item_name
        holder.tvServicePrice.text = currentItem.price

        quantitySet(currentItem,holder.quantity)

        cont = holder.quantity.text.toString().toInt()

        Glide.with(mainActivity).load(currentItem.image).error(R.drawable.ic_launcher_foreground).into(holder.imageView)

        if(currentItem.extra_services.isNotEmpty()){
            holder.rec.layoutManager = LinearLayoutManager(mainActivity)
            val list = mutableListOf<ExtraService>()
            val extraServiceAdapter = ExtraServiceAdapter(mainActivity,list)
            holder.rec.adapter = extraServiceAdapter
            list.addAll(currentItem.extra_services)
            extraServiceAdapter.notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            Log.d("tarun", "onBindViewHolder:1 ")
            if(currentItem.extra_services.isNotEmpty()){
                Log.d("tarun", "onBindViewHolder:2 ")
                if(holder.rec.visibility == View.VISIBLE)
                    holder.rec.visibility = View.GONE
                else
                    holder.rec.visibility = View.VISIBLE
            }

        }


    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvServiceName:TextView = view.findViewById(R.id.tv_service_name)
        val tvServicePrice:TextView = view.findViewById(R.id.tv_service_price)
        val imageView:ImageView = view.findViewById(R.id.image)
        val rec:RecyclerView = view.findViewById(R.id.extra_service_rec)
        val addBtn:View = view.findViewById(R.id.add_btn)
        val minusBtn:View = view.findViewById(R.id.minus_btn)
        val quantity:TextView = view.findViewById(R.id.tv_quantity)

    }


    private fun quantitySet(currentItem:Service, quantity:TextView) {
        val list = mainActivity.getAllDataFromDataBase()
        list.forEach {
            if (it.id == currentItem.item_id) {
                Log.d("tarun", "quantitySet: ")
                quantity.text = it.quantity.toString()
                return
            }else{
                quantity.text = "0"
            }
        }
    }

}