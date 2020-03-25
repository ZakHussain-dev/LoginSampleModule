package com.loginmodule.di.component

import com.loginmodule.BaseApplication
import com.loginmodule.di.builder.ActivityBuilderModule
import com.loginmodule.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Parent DI component for application level dependencies
 */
@Singleton
@Component(modules = [ActivityBuilderModule::class, AndroidSupportInjectionModule::class, AppModule::class])
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: BaseApplication): Builder

        fun build(): AppComponent
    }
}