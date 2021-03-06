package com.linsh.lshapp.mvp.home.shiyi;

import com.linsh.lshapp.base.BaseContract;
import com.linsh.lshapp.model.bean.db.Group;

import java.util.List;

/**
 * Created by Senh Linsh on 17/4/24.
 */

public interface ShiyiContract {


    interface View extends BaseContract.BaseView {

        void setData(List<Group> groups);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        void addGroup(String groupName);

        void deleteGroup(int position);

        void renameGroup(int position, String groupName);

        List<Group> getGroups();

        void syncPersonsInGroup(int position);
    }
}
