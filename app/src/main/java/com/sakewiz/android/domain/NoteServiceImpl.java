package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.request.CreateProductNoteRequest;
import com.sakewiz.android.model.entities.response.CreateProductNoteResponse;
import com.sakewiz.android.model.entities.response.NoteResponse;
import com.sakewiz.android.model.rest.SakeWizService;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public class NoteServiceImpl implements NoteService {

    private SakeWizService sakeWizService;

    public NoteServiceImpl(SakeWizService nzsService) {
        super();
        this.sakeWizService = nzsService;
    }

    @Override
    public Observable<List<NoteResponse>> getNotesService(String authorizationKey, String type) {
        return sakeWizService.getApi()
                .getNotesAPI(authorizationKey, type)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<NoteResponse> getNoteByUserIdAndEntityIdService(String authorizationKey, String entityId) {
        return sakeWizService.getApi()
                .getNoteByUserIdAndEntityIdAPI(authorizationKey, entityId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CreateProductNoteResponse> doCreateProductNoteService(String authorizationKey, String productId, CreateProductNoteRequest createProductNoteRequest) {
        return sakeWizService.getApi()
                .doCreateProductNoteAPI(authorizationKey, productId, createProductNoteRequest)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
