package com.example.krishna

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krishna.dao.ServiceEntity
import com.example.krishna.databinding.ActivityMainBinding
import com.example.krishna.model.ApiResponse
import com.example.krishna.model.Service

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var shopViewModel: ShopViewModel
    private lateinit var itemlist: MutableList<Service>
    private lateinit var list: MutableList<ServiceEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemlist = mutableListOf()
        list = mutableListOf()
        val adapter = ServiceAdapter(this, itemlist)
        binding.serviceRec.layoutManager = LinearLayoutManager(this)
        binding.serviceRec.adapter = adapter

        val serviceViewModelFactory = ServiceViewModelFactory(application)
        shopViewModel = ViewModelProvider(this, serviceViewModelFactory)[ShopViewModel::class.java]

        shopViewModel.getDataFromNetwork()

        shopViewModel.serviceFromNetwork.observe(this, Observer { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    binding.pro.visibility = View.VISIBLE
                    binding.errorMessage.visibility = View.GONE
                    binding.serviceRec.visibility = View.GONE
                }
                is ApiResponse.Success -> {
                    val shops = response.data
                    itemlist.addAll(shops.data.item_list[0].services)
                    adapter.notifyDataSetChanged()

                    binding.pro.visibility = View.GONE
                    binding.errorMessage.visibility = View.GONE
                    binding.serviceRec.visibility = View.VISIBLE

                }
                is ApiResponse.Error -> {
                    binding.pro.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE
                    binding.serviceRec.visibility = View.GONE
                    binding.errorMessage.text = response.exception
                }
            }
        })

        shopViewModel.getDataFromDataBase()

        shopViewModel.serviceFromDatabase.observe(this, Observer { response ->
            when (response) {
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    val services = response.data
                    list.clear()
                    list.addAll(services)
                    setTotalPriceAndTotalItem()
                }
                is ApiResponse.Error -> {}
            }
        })


        binding.btnNext.setOnClickListener {
            val intent = Intent(this,ResultActivity::class.java)
            startActivity(intent)
        }

        setTotalPriceAndTotalItem()
    }

    fun insertData(service:ServiceEntity){
        shopViewModel.insertData(service)
    }

    fun deleteData(id:String){
        shopViewModel.deleteData(id)
    }


    fun updateData(quantity:Int,id: String){
        shopViewModel.updateData(quantity,id)
    }

    fun getAllDataFromDataBase():MutableList<ServiceEntity>{
        return list
    }

    private fun getTotalPrice():Float{
        var totalPrice = 0.0f
        list.forEach {
            totalPrice += it.quantity*it.servicePrice.toFloat()
        }
        return totalPrice
    }

    private fun getTotalItem():Int{
        var totalItem = 0
        list.forEach {
            totalItem += it.quantity
        }
        return totalItem
    }

    private fun setTotalPriceAndTotalItem(){
        binding.tvTotalPrice.text = "Total ₹ ${getTotalPrice()}"
        binding.tvItemsAdded.text = "${getTotalItem()} Items Added"
    }

}