package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.request.CreateProductNoteRequest;
import com.sakewiz.android.model.entities.response.CreateProductNoteResponse;
import com.sakewiz.android.model.entities.response.NoteResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public interface NoteService extends Service {
    Observable<List<NoteResponse>> getNotesService(String authorizationKey, String type);
    Observable<NoteResponse> getNoteByUserIdAndEntityIdService(String authorizationKey, String entityId);
    Observable<CreateProductNoteResponse> doCreateProductNoteService(String authorizationKey, String productId, CreateProductNoteRequest createProductNoteRequest);

}
