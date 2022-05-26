package online.javalab.poly.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import online.javalab.poly.dto.JavaLabServer;
import online.javalab.poly.dto.Repository;

public class BaseViewModel extends ViewModel {
   protected final Repository repository = new Repository(JavaLabServer.getApiService());
   protected final MutableLiveData<Boolean> loading = new MutableLiveData<>();
   protected CompositeDisposable mDisposable;

    public BaseViewModel(){
        mDisposable = new CompositeDisposable();
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        if (mDisposable != null) {
            mDisposable.clear();
            mDisposable = null;
        }
    }
}
