package com.drathonix.dubiousdevices.interfaces;

public interface INotifiable<T> {
    void notify(INotifier<T> sender, T status);
}
