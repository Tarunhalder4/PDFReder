package com.example.krishna

import com.example.krishna.dao.ServiceDao
import com.example.krishna.dao.ServiceEntity
import com.example.krishna.model.Data

class ShopRepository(private val serviceDao: ServiceDao){

    suspend fun getServiceFromNetwork():Data? {
        val apiService = ApiClient.getRetrofitInstance()?.create(ApiService::class.java)
        return apiService?.getItemByShopId("ST3f14")
    }

    suspend fun insert(service:ServiceEntity) = serviceDao.insert(service)
    suspend fun getServiceFromDataBase() = serviceDao.getService()
    suspend fun deleteData(id:String) = serviceDao.delete(id)
    suspend fun updateData(quantity:Int, id: String) = serviceDao.update(quantity,id)

}
