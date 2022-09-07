package com.orgzly.android.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orgzly.android.ui.SingleLiveEvent
import com.orgzly.android.App
import com.orgzly.android.usecase.NoteUpdateClockingState
import com.orgzly.android.usecase.UseCase
import com.orgzly.android.usecase.UseCaseRunner

class SharedMainActivityViewModel : ViewModel() {
    data class FragmentState(val tag: String)

    val currentFragmentState: MutableLiveData<FragmentState> = MutableLiveData()

    fun setCurrentFragment(tag: String) {
        currentFragmentState.value = FragmentState(tag)
    }

    val drawerLockState: MutableLiveData<Boolean> = MutableLiveData()

    fun lockDrawer() {
        drawerLockState.value = true
    }

    fun unlockDrawer() {
        drawerLockState.value = false
    }

    val openDrawerRequest: SingleLiveEvent<Boolean> = SingleLiveEvent()
    fun openDrawer() {
        openDrawerRequest.postValue(true)
    }

    fun run(action: UseCase?) {
        App.EXECUTORS.diskIO().execute {
            try {
                UseCaseRunner.run(action!!)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun clockingUpdateRequest(noteIds: Set<Long>, type: Int) {
        run(NoteUpdateClockingState(noteIds, type))
    }
}
