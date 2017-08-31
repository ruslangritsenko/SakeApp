package com.sakewiz.android.mvp.views;


import com.sakewiz.android.model.entities.response.CreateProductNoteResponse;
import com.sakewiz.android.model.entities.response.NoteResponse;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public interface NoteView extends View{
    void showNotesResponse(Object response);
    void showNoteByUserIdAndEntityIdResponse (NoteResponse notesResponse);
    void showCreateProductNoteResponse(CreateProductNoteResponse createProductNoteResponse);
}
