package com.linsh.lshapp.part.detail;

import com.linsh.lshapp.base.BaseContract;
import com.linsh.lshapp.model.bean.Person;
import com.linsh.lshapp.model.bean.PersonDetail;
import com.linsh.lshapp.model.bean.TypeLabel;

import io.realm.RealmList;

/**
 * Created by Senh Linsh on 17/4/25.
 */

public interface PersonDetailContract {

    interface View extends BaseContract.BaseView {

        String getPersonId();

        void setData(Person person);

        void setData(PersonDetail personDetail);

    }

    interface Presenter extends BaseContract.BasePresenter<View> {

        RealmList<TypeLabel> getTypes();

        void addTypeLabel(String inputText);

        void addType(String typeName);
    }
}