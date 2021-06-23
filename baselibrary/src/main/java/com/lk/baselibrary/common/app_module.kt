package com.lk.baselibrary.common

import org.koin.dsl.module

/**
 * @author LK
 * @date 2019-12-31
 */
val viewModelModule = module {


}
val remoteModule = module {

}

val localModule = module {


}

val repoModule = module {

}
val appModule = listOf(viewModelModule, localModule, remoteModule, repoModule)