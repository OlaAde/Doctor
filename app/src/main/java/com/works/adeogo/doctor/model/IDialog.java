package com.works.adeogo.doctor.model;

import java.util.List;

/**
 * Created by ademi on 03/04/2018.
 */

public interface IDialog<MESSAGE extends IMessage> {

    String getId();

    String getDialogPhoto();

    String getDialogName();

    List<? extends IUser> getUsers();

    MESSAGE getLastMessage();

    void setLastMessage(MESSAGE message);

}