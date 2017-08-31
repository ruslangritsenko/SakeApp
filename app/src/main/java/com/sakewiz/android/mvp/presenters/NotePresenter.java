package com.sakewiz.android.mvp.presenters;

import com.sakewiz.android.model.entities.request.CreateProductNoteRequest;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public interface NotePresenter extends Presenter {
    void getNotes(String type);
    void getNoteByUserIdAndEntityId(String entityId);
    void doCreateProductNote(String productId, CreateProductNoteRequest createProductNoteRequest);
}
