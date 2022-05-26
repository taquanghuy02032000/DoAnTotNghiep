package online.javalab.poly.viewmodel;

import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import online.javalab.poly.base.BaseViewModel;
import online.javalab.poly.base.base.ListResponse;
import online.javalab.poly.model.Profile;
import online.javalab.poly.model.TopLesson;
import online.javalab.poly.model.rank.Datum;

public class ProfileViewModel extends BaseViewModel {
    private final MutableLiveData<ListResponse<Profile>> profileListData = new MutableLiveData<>();
    private final MutableLiveData<ListResponse<Datum>> datumList = new MutableLiveData<>();
    private final MutableLiveData<ListResponse<TopLesson>> topLessons = new MutableLiveData<>();

    public MutableLiveData<ListResponse<Profile>> getProfileScore(String userId) {
        return profileListData;
    }

    public MutableLiveData<ListResponse<Datum>> getDatumList() {
        return datumList;
    }

    public MutableLiveData<ListResponse<TopLesson>> getTopLessons() {
        return topLessons;
    }

    public void loadProfileScores(String userId) {
        mDisposable.add(
                repository.getProfileScore(userId)
                        .doOnSubscribe(disposable -> profileListData.setValue(new ListResponse<Profile>().loading()))
                        .subscribeWith(new DisposableSingleObserver<ListResponse<Profile>>() {
                            @Override
                            public void onSuccess(@NonNull ListResponse<Profile> profileListResponse) {
                                assert profileListResponse.getData() != null;
                                profileListData.setValue(new ListResponse<Profile>().success(profileListResponse.getData()));
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                profileListData.setValue(new ListResponse<Profile>().error(e));
                            }
                        })

        );
    }

    public void loadProfileRank() {
        mDisposable.add(
                repository.getProfileRank()
                        .doOnSubscribe(disposable -> datumList.setValue(new ListResponse<Datum>().loading()))
                        .subscribeWith(new DisposableSingleObserver<ListResponse<Datum>>() {
                            @Override
                            public void onSuccess(@NonNull ListResponse<Datum> datumListResponse) {
                                if (datumListResponse.getData() != null) {
                                    datumList.setValue(new ListResponse<Datum>().success(datumListResponse.getData()));
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                datumList.setValue(new ListResponse<Datum>().error(e));
                            }
                        })
        );
    }

    public void loadTopLesson(String userId) {
        mDisposable.add(
                repository.getTopLesson(userId)
                        .doOnSubscribe(disposable -> topLessons.setValue(new ListResponse<TopLesson>().loading()))
                        .subscribeWith(new DisposableSingleObserver<ListResponse<TopLesson>>() {
                            @Override
                            public void onSuccess(@NonNull ListResponse<TopLesson> listResponse) {
                                if (listResponse.getData() != null) {
                                    topLessons.postValue(new ListResponse<TopLesson>().success(listResponse.getData()));
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                topLessons.postValue(new ListResponse<TopLesson>().error(e));
                            }
                        })
        );
    }
}
