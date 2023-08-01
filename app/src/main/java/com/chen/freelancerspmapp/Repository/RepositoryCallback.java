package com.chen.freelancerspmapp.Repository;

import androidx.lifecycle.LiveData;

public interface RepositoryCallback<T> {
    void onComplete(LiveData<T> queryResult);
}
