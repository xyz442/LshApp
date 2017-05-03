package com.linsh.lshapp.part.home.shiyi;

import com.linsh.lshapp.base.BasePresenterImpl;
import com.linsh.lshapp.model.bean.Group;
import com.linsh.lshapp.model.throwabes.DeleteUnemptyGroupThrowable;
import com.linsh.lshapp.model.throwabes.DeleteUnnameGroupThrowable;
import com.linsh.lshapp.tools.ShiyiDataOperator;
import com.linsh.lshutils.view.LshColorDialog;

import io.realm.RealmList;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Senh Linsh on 17/4/24.
 */

public class ShiyiPresenter extends BasePresenterImpl<ShiyiContract.View> implements ShiyiContract.Presenter {

    private RealmList<Group> mGroups;

    @Override
    protected void attachView() {
        getGroups();
    }

    @Override
    public void subscribe() {
        super.subscribe();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void getGroups() {
        Subscription subscription = ShiyiDataOperator.getGroups(getRealm())

                .subscribe(new Action1<RealmList<Group>>() {
                    @Override
                    public void call(RealmList<Group> groups) {
                        mGroups = groups;
                        getView().setData(groups);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        getView().showToast(throwable.getMessage());
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void addGroup(String groupName) {
        getView().showLoadingDialog();
        ShiyiDataOperator.addGroup(getRealm(), groupName)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getView().dismissLoadingDialog();
                        getView().setData(mGroups);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        getView().dismissLoadingDialog();
                        getView().showToast(throwable.getMessage());
                    }
                });
    }

    @Override
    public void deleteGroup(int position) {
        getView().showLoadingDialog();
        final Group group = mGroups.get(position);

        ShiyiDataOperator.deleteGroup(getRealm(), group.getId())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getView().dismissLoadingDialog();
                        getView().setData(mGroups);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getView().dismissLoadingDialog();
                        if (throwable instanceof DeleteUnnameGroupThrowable) {
                            deleteUnnameGroup();
                        } else if (throwable instanceof DeleteUnemptyGroupThrowable) {
                            deleteUnemptyGroup(group.getId());
                        } else {
                            throwable.printStackTrace();
                            getView().showToast(throwable.getMessage());
                        }
                    }
                });
    }

    private void deleteUnnameGroup() {
        getView().showTextDialog("未分组里的联系人必须移至其他分组后才能删除");
    }

    private void deleteUnemptyGroup(final String groupId) {
        getView().showTextDialog("分组中的联系人不会被删除，是否继续删除该分组？", null, null,
                "删除", new LshColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(LshColorDialog dialog) {
                        ShiyiDataOperator.moveToUnnameGroup(getRealm(), groupId);
                    }
                });
    }

    @Override
    public void renameGroup(int position, String groupName) {
        getView().showLoadingDialog();
        Group group = mGroups.get(position);

        if (!group.getName().equals(groupName)) {
            ShiyiDataOperator.renameGroup(getRealm(), group.getId(), groupName)
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            getView().dismissLoadingDialog();
                            getView().setData(mGroups);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            getView().dismissLoadingDialog();
                            throwable.printStackTrace();
                            getView().showToast(throwable.getMessage());
                        }
                    });
        }
    }
}