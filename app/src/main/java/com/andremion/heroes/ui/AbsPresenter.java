/*
 * Copyright (c) 2017. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andremion.heroes.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

public abstract class AbsPresenter<V> {

    protected V mView;

    protected AbsPresenter() {
    }

    /**
     * Call this method in {@link Activity#onCreate}
     * or {@link Fragment#onAttach(Context)} to attach the MVP View object
     */
    @CallSuper
    public void attachView(@NonNull V view) {
        mView = view;
    }

    /**
     * Call this method in {@link Activity#onDestroy()}
     * or {@link Fragment#onDetach()} to detach the MVP View object
     */
    @CallSuper
    public void detachView() {
        mView = null;
    }

    /**
     * Check if the view is attached.
     * This checking is only necessary when returning from an asynchronous call
     */
    protected final boolean isViewAttached() {
        return mView != null;
    }

}
