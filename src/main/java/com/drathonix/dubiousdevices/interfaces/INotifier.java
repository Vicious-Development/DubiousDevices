package com.drathonix.dubiousdevices.interfaces;

public interface INotifier<T> {
    void sendNotification(T status);
    void listen(INotifiable<T> notifiable);
    void stopListening(INotifiable<T> notifiable);
}
