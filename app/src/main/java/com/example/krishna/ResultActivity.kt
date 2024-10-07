package com.example.krishna

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krishna.dao.ServiceEntity
import com.example.krishna.databinding.ResultActivityBinding
import com.example.krishna.model.ApiResponse

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ResultActivityBinding
    private lateinit var list: MutableList<ServiceEntity>
    private lateinit var shopViewModel: ShopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ResultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceViewModelFactory = ServiceViewModelFactory(application)
        shopViewModel = ViewModelProvider(this, serviceViewModelFactory)[ShopViewModel::class.java]

        list = mutableListOf()
        val adapter = ResultAdapter(list)
        binding.rec.layoutManager = LinearLayoutManager(this)
        binding.rec.adapter = adapter

        shopViewModel.getDataFromDataBase()

        shopViewModel.serviceFromDatabase.observe(this, Observer { response ->
            when (response) {
                is ApiResponse.Loading -> {
                    binding.pro.visibility = View.VISIBLE
                    binding.errorMessage.visibility = View.GONE
                    binding.rec.visibility = View.GONE
                }
                is ApiResponse.Success -> {
                    val services = response.data
                    list.addAll(services)
                    adapter.notifyDataSetChanged()

                    binding.pro.visibility = View.GONE
                    binding.errorMessage.visibility = View.GONE
                    binding.rec.visibility = View.VISIBLE

                    binding.discount.text =  String.format("%.2f", getDiscount())
                    binding.finalAmount.text = String.format("%.2f", (getTotalAmount() - getDiscount()))
                    binding.subTotal.text =  String.format("%.2f", getTotalAmount())

                }
                is ApiResponse.Error -> {
                    binding.pro.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE
                    binding.rec.visibility = View.GONE
                    binding.errorMessage.text = response.exception
                }
            }
        })




    }


    private fun getTotalAmount():Float{
        var totalAmount = 0.0f
        list.forEach {
            totalAmount += (it.quantity*it.servicePrice.toFloat())
        }
        return totalAmount
    }

    private fun getDiscountedTotalAmount():Float{
        var totalAmount = 0.0f
        list.forEach {
            if(it.discountPercentage.isNotEmpty()){
                val actualPercentage = 100-it.discountPercentage.toFloat()
                totalAmount += it.quantity*((it.servicePrice.toFloat() * actualPercentage) / 100)
            }else{
                totalAmount += it.quantity*it.servicePrice.toFloat()
            }
        }
        return totalAmount
    }

    private fun getDiscount():Float{
        return getTotalAmount() - getDiscountedTotalAmount()
    }


}