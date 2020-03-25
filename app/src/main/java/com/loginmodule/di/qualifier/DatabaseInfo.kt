package com.loginmodule.di.qualifier

import javax.inject.Qualifier

/**
 * Annotation Qualifier for differentiating similar providers.
 *
 */
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class DatabaseInfo