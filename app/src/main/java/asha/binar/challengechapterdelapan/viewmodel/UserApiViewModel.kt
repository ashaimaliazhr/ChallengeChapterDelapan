package asha.binar.challengechapterdelapan.viewmodel

import androidx.lifecycle.*
import asha.binar.challengechapterdelapan.data.datastore.DataStoreManager
import asha.binar.challengechapterdelapan.data.room.Favorite
import asha.binar.challengechapterdelapan.data.utils.MainRepository
import asha.binar.challengechapterdelapan.data.utils.Resource
import asha.binar.challengechapterdelapan.data.utils.SingleLiveEvent
import asha.binar.challengechapterdelapan.model.users.GetUserResponseItem
import asha.binar.challengechapterdelapan.model.users.PostUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserApiViewModel  @Inject constructor(
    private val mainRepository: MainRepository,
    private val pref: DataStoreManager
) : ViewModel(){
    val user = MutableLiveData<GetUserResponseItem>()
    val loginStatus = SingleLiveEvent<Boolean>()
    val registerCheck = SingleLiveEvent<Boolean>()

    fun setEmail(email: String) {
        viewModelScope.launch {
            pref.setUser(email)
        }
    }

    fun getEmail(): LiveData<String> {
        return pref.getUser().asLiveData()
    }

    fun setImage(img: String) {
        viewModelScope.launch {
            pref.setImageCamera(img)
        }
    }

    fun getImage(): LiveData<String> {
        return pref.getImage().asLiveData()
    }

    fun getUser(email: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.getUser(email)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occured!"))
        }
    }

    fun registerUser(user: PostUserResponse) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.addUsers(user)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occured!"))
        }
    }

    fun updateUser(user: PostUserResponse, id: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(mainRepository.updateUser(user, id)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occured!"))
        }
    }

    fun addFavorite(favorite: Favorite) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.addFavorite(favorite)
    }

    fun deleteFavorite(favorite: Favorite) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.deleteFavorite(favorite)
    }

    fun getFavorite(email: String) = mainRepository.getFavorite(email)
}