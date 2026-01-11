// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util.messages;

import com.intellij.openapi.Disposable;
//import com.intellij.openapi.vfs.Topic;
import com.intellij.util.messages.Topic

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Aggregates multiple topic subscriptions for particular {@link MessageBus}. I.e., every time a client wants to
 * listen for messages, it should grab the appropriate connection (or create a new one) and {@link #subscribe(Topic, Object)}
 * to particular endpoint.
 */
public interface MessageBusConnection extends SimpleMessageBusConnection, Disposable {
  /**
   * Subscribes to the target topic within the current connection using {@link #setDefaultHandler(MessageHandler)}.
   *
   * @param topic target endpoint
   * @param <L>   interface for working with the target topic
   * @throws IllegalStateException if {@link #setDefaultHandler(MessageHandler)} hasn't been defined or
   *                               has an incompatible type with the {@link Topic#getListenerClass()}
   */
  <L> void subscribe(@NotNull Topic<L> topic);

  /**
   * Allows specifying the default handler to use during anonymous subscriptions {@link #subscribe(Topic)}.
   */
  void setDefaultHandler(@Nullable MessageHandler handler);

  default void setDefaultHandler(@NotNull Runnable runnable) {
    setDefaultHandler((event, params) -> runnable.run());
  }

  /**
   * Forces to process any queued but not delivered events.
   *
   * @see MessageBus#syncPublisher(Topic)
   */
  void deliverImmediately();
}
