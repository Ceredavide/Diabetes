package ch.hslu.mobpro.diabetes.ui.manualenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EnterManualViewModel : ViewModel(){

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Fragment for entering products manually"
    }
    val text: LiveData<String> = _text
}