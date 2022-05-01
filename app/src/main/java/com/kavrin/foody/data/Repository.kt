package com.kavrin.foody.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped // Scope annotation for bindings that should exist for the life of an activity, surviving configuration. Repository will be injected in ViewModel
class Repository @Inject constructor(
    val remoteDataSource: RemoteDataSource
)