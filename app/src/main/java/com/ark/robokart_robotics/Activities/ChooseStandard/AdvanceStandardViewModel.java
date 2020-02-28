package com.ark.robokart_robotics.Activities.ChooseStandard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ark.robokart_robotics.Model.StandardModel;

import java.util.List;

public class AdvanceStandardViewModel extends AndroidViewModel {

    private AdvanceStandardRepository advanceStandardRepository;

    public AdvanceStandardViewModel(@NonNull Application application) {
        super(application);
        advanceStandardRepository = new AdvanceStandardRepository(application);
    }

    public MutableLiveData<List<StandardModel>> getAdvance() {
        return advanceStandardRepository.getAdvancedList();
    }
}
