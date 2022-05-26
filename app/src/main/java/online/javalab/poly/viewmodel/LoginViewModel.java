package online.javalab.poly.viewmodel;
import androidx.lifecycle.MutableLiveData;
import online.javalab.poly.base.BaseViewModel;
import online.javalab.poly.base.base.ObjectResponse;
import online.javalab.poly.model.User;

public class LoginViewModel extends BaseViewModel {
    private final MutableLiveData<ObjectResponse<User>> userResponse = new MutableLiveData<>();
    public MutableLiveData<ObjectResponse<User>> getUserResponse() {
        return userResponse;
    }

    public void getOrCreateNewUser(User user){
        mDisposable.add(
                repository.getOrCreateNewUser(user)
                .doOnSubscribe(disposable -> {
                    userResponse.setValue(new ObjectResponse<User>().loading());
                })
                .subscribe(this::onNext, this::onError)
        );
    }

    private void onError(Throwable throwable) {
     userResponse.setValue(new ObjectResponse<User>().error(throwable));
    }

    private void onNext(ObjectResponse<User> response) {
        if (response.getData() != null) {
            userResponse.setValue(new ObjectResponse<User>().success(response.getData()));
        }
    }

}
