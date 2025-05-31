package com.rxjava;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Observable<T> {
    
    public abstract Subscription subscribe(Observer<T> observer);
    
    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new Observable<T>() {
            @Override
            public Subscription subscribe(Observer<T> observer) {
                try {
                    source.subscribe(observer);
                    return new Subscription() {
                        private boolean unsubscribed = false;
                        
                        @Override
                        public void unsubscribe() {
                            unsubscribed = true;
                        }
                        
                        @Override
                        public boolean isUnsubscribed() {
                            return unsubscribed;
                        }
                    };
                } catch (Exception e) {
                    observer.onError(e);
                    return new Subscription() {
                        @Override
                        public void unsubscribe() {}
                        
                        @Override
                        public boolean isUnsubscribed() {
                            return true;
                        }
                    };
                }
            }
        };
    }
    
    public <R> Observable<R> map(Function<T, R> mapper) {
        return new Observable<R>() {
            @Override
            public Subscription subscribe(Observer<R> observer) {
                return Observable.this.subscribe(new Observer<T>() {
                    @Override
                    public void onNext(T item) {
                        try {
                            R mapped = mapper.apply(item);
                            observer.onNext(mapped);
                        } catch (Exception e) {
                            observer.onError(e);
                        }
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        observer.onError(t);
                    }
                    
                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
            }
        };
    }
    
    public Observable<T> filter(Predicate<T> predicate) {
        return new Observable<T>() {
            @Override
            public Subscription subscribe(Observer<T> observer) {
                return Observable.this.subscribe(new Observer<T>() {
                    @Override
                    public void onNext(T item) {
                        try {
                            if (predicate.test(item)) {
                                observer.onNext(item);
                            }
                        } catch (Exception e) {
                            observer.onError(e);
                        }
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        observer.onError(t);
                    }
                    
                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
            }
        };
    }
    
    public <R> Observable<R> flatMap(Function<T, Observable<R>> mapper) {
        return new Observable<R>() {
            @Override
            public Subscription subscribe(Observer<R> observer) {
                return Observable.this.subscribe(new Observer<T>() {
                    @Override
                    public void onNext(T item) {
                        try {
                            Observable<R> inner = mapper.apply(item);
                            inner.subscribe(new Observer<R>() {
                                @Override
                                public void onNext(R innerItem) {
                                    observer.onNext(innerItem);
                                }
                                
                                @Override
                                public void onError(Throwable t) {
                                    observer.onError(t);
                                }
                                
                                @Override
                                public void onComplete() {
                                    // Don't complete outer observer on inner completion
                                }
                            });
                        } catch (Exception e) {
                            observer.onError(e);
                        }
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        observer.onError(t);
                    }
                    
                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
            }
        };
    }
}