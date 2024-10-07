package com.example.krishna

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.krishna.dao.DataBase
import com.example.krishna.dao.ServiceEntity
import com.example.krishna.model.ApiResponse
import com.example.krishna.model.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopViewModel(application: Application) : AndroidViewModel(application){

    private val userDao = DataBase.getDatabase(application).userDao()
    private val serviceRepository = ShopRepository(userDao)

    private val _serviceFromNetwork = MutableLiveData<ApiResponse<Data>>()
    val serviceFromNetwork: LiveData<ApiResponse<Data>> get() = _serviceFromNetwork

    private val _serviceFromDatabase = MutableLiveData<ApiResponse<List<ServiceEntity>>>()
    val serviceFromDatabase: LiveData<ApiResponse<List<ServiceEntity>>> get() = _serviceFromDatabase


    fun getDataFromNetwork() {
        _serviceFromNetwork.postValue(ApiResponse.Loading)
        viewModelScope.launch {
            try {
                _serviceFromNetwork.postValue(serviceRepository.getServiceFromNetwork()?.let { ApiResponse.Success(it) })
            } catch (e: Exception) {
                _serviceFromNetwork.postValue(e.message?.let { ApiResponse.Error(it) })
            }
        }
    }

    fun getDataFromDataBase(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _serviceFromDatabase.postValue(ApiResponse.Success(serviceRepository.getServiceFromDataBase()))
            } catch (e: Exception) {
                _serviceFromDatabase.postValue(e.message?.let { ApiResponse.Error(it) })
            }
        }
    }


    fun insertData(serviceEntity: ServiceEntity){
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.insert(serviceEntity)
        }
    }

    fun updateData(quantity:Int,id:String){
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.updateData(quantity,id)
        }
    }


    fun deleteData(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.deleteData(id)
        }
    }


}