package com.sakewiz.android.mvp.presenters;

import android.app.Activity;
import android.util.Log;

import com.sakewiz.android.common.constants.ApplicationConstants;
import com.sakewiz.android.domain.NoteService;
import com.sakewiz.android.domain.Service;
import com.sakewiz.android.model.entities.request.CreateProductNoteRequest;
import com.sakewiz.android.model.entities.response.CreateProductNoteResponse;
import com.sakewiz.android.model.entities.response.NoteResponse;
import com.sakewiz.android.model.entities.response.NotesResponse;
import com.sakewiz.android.model.rest.exception.RetrofitException;
import com.sakewiz.android.mvp.views.NoteView;
import com.sakewiz.android.mvp.views.View;
import com.sakewiz.android.utils.IScheduler;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by dilshan_e on 29/05/2017.
 */

public class NotePresenterImpl extends BasePresenter implements NotePresenter {

    private final static String TAG = "NotePresenterImpl";

    private NoteView mNoteView;

    public NotePresenterImpl(Activity activityContext, Service noteService, IScheduler scheduler) {
        super(activityContext, noteService, scheduler);
    }
    @Override
    public void getNotes(String type) {
        subscription = getNotesObservable(type).subscribe(getNotesSubscriber());
    }

    @Override
    public void getNoteByUserIdAndEntityId(String entityId) {
        subscription = getNoteByUserIdAndEntityIdObservable(entityId).subscribe(getNoteByUserIdAndEntityIdSubscriber());
    }

    @Override
    public void doCreateProductNote(String productId, CreateProductNoteRequest createProductNoteRequest) {
        subscription = doCreateProductNoteObservable(productId, createProductNoteRequest).subscribe(doCreateProductNoteSubscriber());
    }

    private NoteService getService() {
        return (NoteService) mService;
    }

    @Override
    public void attachView(View v) {
        if (v instanceof NoteView) {
            mNoteView = (NoteView) v;
            mView = mNoteView;
        }
    }

    public Subscriber<List<NoteResponse>> getNotesSubscriber() {
        return new DefaultSubscriber<List<NoteResponse>>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    NotesResponse response = error.getErrorBodyAs(NotesResponse.class);
                    if (response == null) {
                        response = new NotesResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mNoteView.showNotesResponse(response);
                } catch (IOException ex) {
                    NotesResponse exceptionResponse = new NotesResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mNoteView.showNotesResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(List<NoteResponse> response) {
                if (response != null) {
//                    response.setSuccess(true);
                    mNoteView.showNotesResponse(response);
                }
            }
        };
    }

    public Observable<List<NoteResponse>> getNotesObservable(String type) {
        try {
            return getService().getNotesService(getAccessToken(), type)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<NoteResponse> getNoteByUserIdAndEntityIdSubscriber() {
        return new DefaultSubscriber<NoteResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    NoteResponse response = error.getErrorBodyAs(NoteResponse.class);
                    if (response == null) {
                        response = new NoteResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mNoteView.showNoteByUserIdAndEntityIdResponse(response);
                } catch (IOException ex) {
                    NoteResponse exceptionResponse = new NoteResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mNoteView.showNoteByUserIdAndEntityIdResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(NoteResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mNoteView.showNoteByUserIdAndEntityIdResponse(response);
                }
            }
        };
    }

    public Observable<NoteResponse> getNoteByUserIdAndEntityIdObservable(String entityId) {
        try {
            return getService().getNoteByUserIdAndEntityIdService(getAccessToken(), entityId)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    public Subscriber<CreateProductNoteResponse> doCreateProductNoteSubscriber() {
        return new DefaultSubscriber<CreateProductNoteResponse>(this.mView) {

            @Override
            public void onError(Throwable e) {
                try {
                    RetrofitException error = (RetrofitException) e;
                    CreateProductNoteResponse response = error.getErrorBodyAs(CreateProductNoteResponse.class);
                    if (response == null) {
                        response = new CreateProductNoteResponse();
                        response.setMessage(getExceptionMessage(e));
                        response.setAPIError(false);
                    } else {
                        response.setAPIError(true);
                    }
                    response.setSuccess(false);
                    mNoteView.showCreateProductNoteResponse(response);
                } catch (IOException ex) {
                    CreateProductNoteResponse exceptionResponse = new CreateProductNoteResponse();
                    exceptionResponse.setSuccess(false);
                    exceptionResponse.setAPIError(false);
                    exceptionResponse.setMessage(ApplicationConstants.ERROR_MSG_REST_UNEXPECTED);
                    mNoteView.showCreateProductNoteResponse(exceptionResponse);

                    ex.printStackTrace();
                }
            }

            @Override
            public void onNext(CreateProductNoteResponse response) {
                if (response != null) {
                    response.setSuccess(true);
                    mNoteView.showCreateProductNoteResponse(response);
                }
            }
        };
    }

    public Observable<CreateProductNoteResponse> doCreateProductNoteObservable(String productId, CreateProductNoteRequest createProductNoteRequest) {
        try {
            return getService().doCreateProductNoteService(getAccessToken(), productId, createProductNoteRequest)
                    .subscribeOn(scheduler.backgroundThread())
                    .observeOn(scheduler.mainThread());

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
