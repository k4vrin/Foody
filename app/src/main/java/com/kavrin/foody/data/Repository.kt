package com.kavrin.foody.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * Repository
 *
 * Tell hilt how to provide Repository for our [com.kavrin.foody.viewmodels.MainViewModel]
 *
 * To perform field injection, Hilt needs to know how to provide instances of the necessary dependencies
 * from the corresponding component.
 * A binding contains the information necessary to provide instances of a type as a dependency.
 *
 * One way to provide binding information to Hilt is constructor injection.
 * Use the @Inject annotation on the constructor of a class to tell Hilt how to provide instances of that class
 *
 * The parameters of an annotated constructor of a class are the dependencies of that class
 * Therefore, Hilt must also know how to provide instances of the parameter
 *
 * https://developer.android.com/training/dependency-injection/hilt-android#component-scopes
 *
 * This class will survive configuration
 */
@ActivityRetainedScoped // Scope annotation for bindings that should exist for the life of an activity, surviving configuration.
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    val local = localDataSource
}