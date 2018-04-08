package com.works.adeogo.doctor.model;

/**
 * Created by ademi on 03/04/2018.
 */

public interface MessageContentType extends IMessage {

    /**
     * Default media type for image message.
     */
    interface Image extends IMessage {
        String getImageUrl();
    }

// other default types will be here
 }