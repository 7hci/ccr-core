package com.cicuro.core

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object EventBus {
    val publishSubject: BehaviorSubject<Event> = BehaviorSubject.create()

    inline fun <reified T : Event> register(): Observable<T> {
        return publishSubject
            .filter { event -> event::class == T::class }
            .map { obj -> obj as T }
    }

    fun post(event: Event) {
        publishSubject.onNext(event)
    }

    interface Event
}

class GenericErrorEvent : EventBus.Event

class LanguageChangedEvent : EventBus.Event