package com.example.ocandroidp12.ui.all

import android.util.Log
import com.example.ocandroidp12.domain.usecase.GetAllClothesUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocandroidp12.domain.model.Cloth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class ClothesState {
    object Loading : ClothesState()
    data class Success(val clothes: List<Cloth>) : ClothesState()
    data class Error(val message: String) : ClothesState()
}

@HiltViewModel
class AllViewModel @Inject constructor(
    private val allClothesUseCase: GetAllClothesUseCase
)  : ViewModel() {
    private val _state = MutableStateFlow<ClothesState>(ClothesState.Loading)
    val state: StateFlow<ClothesState> = _state

    private val _cloth = MutableStateFlow<Cloth?>(null)
    val cloth: StateFlow<Cloth?> get() = _cloth

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _clothesList = MutableStateFlow<List<Cloth>>(listOf())

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()


    init {
        fetchClothes()
    }

    fun groupByCategory(clothes: List<Cloth>): Map<String, List<Cloth>> {
        return clothes.groupBy { it.category }
    }

    private fun fetchClothes() {
        viewModelScope.launch {
            try {
                val clothes = allClothesUseCase.execute()
                _clothesList.value = clothes
                _state.value = ClothesState.Success(clothes)
                Log.e("AllViewModel", "Clothes fetched: ${_clothesList.value.size}")
            } catch (e: Exception) {
                _state.value = ClothesState.Error(e.message ?: "Unknown error")
                Log.e("AllViewModel", "Error fetching clothes: ${e.message}")
            }
        }
    }



    val filteredClothes: StateFlow<List<Cloth>> = searchText
        .combine(_clothesList) { text, clothes ->
            if (text.isBlank()) {
                clothes
            } else {
                clothes.filter { cloth ->
                    cloth.name.contains(text.trim(), ignoreCase = true)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    fun loadClothById(id: Int) {
        viewModelScope.launch {
            try {
                val clothList = allClothesUseCase.execute()
                _cloth.value = clothList.find { it.id == id }
            } catch (e: IOException) {
                _cloth.value = null
            } catch (e: HttpException) {
                _cloth.value = null
            }
        }
    }
}
