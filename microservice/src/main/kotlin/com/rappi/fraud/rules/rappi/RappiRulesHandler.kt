package com.rappi.fraud.rules.rappi

import io.reactivex.Single

class RappiRulesHandler {
    fun handle(): Single<Boolean> {
        return Single.just(true)
    }
}
