// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util.messages;

import com.intellij.openapi.Disposable;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Core of IntelliJ Platform messaging infrastructure. Basic functions:
 *  * allows to {@link #syncPublisher(Topic) push messages};
 *  * allows to {@link #connect() create connections} for further {@link MessageBusConnection#subscribe(Topic, Object) subscriptions};
 * <p>
 * Use {@code com.intellij.openapi.components.ComponentManager.getMessageBus()} to obtain one.
 * <p>
 * Please see <a href="https://plugins.jetbrains.com/docs/intellij/messaging-infrastructure.html">Messaging Infrastructure</a> and
 * <a href="https://plugins.jetbrains.com/docs/intellij/plugin-listeners.html">Listeners</a>.
 */
public interface MessageBus extends Disposable {
  /**
   * Message buses can be organised into hierarchies. That allows facilities {@link Topic#getBroadcastDirection() broadcasting}.
   * The current method exposes parent bus (if any is defined).
   */
  @Nullable
  MessageBus getParent();

  /**
   * Create a new {@link Disposable} connection that is disconnected on message bus dispose, or on explicitly dispose.
   */
  @NotNull
  MessageBusConnection connect();

  /**
   * Create a new connection that is disconnected on message bus dispose, or on explicit {@link SimpleMessageBusConnection#disconnect()}.
   */
  @Internal
  @NotNull
  SimpleMessageBusConnection simpleConnect();

  /**
   * Allows creating new connection that is bound to the given {@link Disposable}.
   * That means that returned connection
   * will be automatically {@link MessageBusConnection#dispose() released} if given {@code parentDisposable} is collected.
   *
   * @param parentDisposable target parent disposable to which life cycle newly created connection shall be bound
   */
  @NotNull
  MessageBusConnection connect(@NotNull Disposable parentDisposable);

  @NotNull
  SimpleMessageBusConnection connect(@NotNull CoroutineScope coroutineScope);

  /**
   * Allows retrieving an interface for publishing messages to the target topic.
   * <p>
   * Basically, the whole processing looks as follows:
   * <ol>
   *  <li>Messaging clients create new {@link MessageBusConnection connections} within the target message bus and
   * {@link MessageBusConnection#subscribe(Topic, Object) subscribe} to the target {@link Topic topics};</li>
   *
   *  <li>Every time somebody wants to send a message for a particular topic, he or she calls current method and receives an object
   * that conforms to the {@link Topic#getListenerClass() business interface} of the target topic. Every method call on that
   * object is dispatched by the messaging infrastructure to the subscribers.
   * {@link Topic#getBroadcastDirection() broadcasting} is performed if necessary as well;</li>
   * </ol>
   * <p>
   * It's also very important to understand message processing strategy in case of <b>nested dispatches</b>.
   * Consider the following situation:
   * <ol>
   *  <li>{@code Subscriber1} and {@code subscriber2} are registered for the same topic within the same message bus;</li>
   *  <li>{@code Message1} is sent to that topic within the same message bus;</li>
   *  <li>Queued message delivery starts;</li>
   *  <li>Queued message delivery ends as there are no messages queued but not dispatched;</li>
   *  <li>{@code Message1} is queued for delivery to both subscribers;</li>
   *  <li>Queued messages delivery starts;</li>
   *  <li>{@code Message1} is being delivered to the {@code subscriber1};</li>
   *  <li>{@code Subscriber1} sends {@code message2} to the same topic within the same bus;</li>
   *  <li>Queued messages delivery starts;</li>
   * </ol>
   * <b>Important:</b> {@code subscriber2} is being notified about all queued but not delivered messages,
   * i.e., its callback is invoked for the {@code message1};
   * <ol type="a" start="10">
   *  <li>Queued messages delivery ends because all subscribers have been notified on the {@code message1};</li>
   *  <li>{@code Message2} is queued for delivery to both subscribers;</li>
   *  <li>Queued messages delivery starts;</li>
   *  <li>{@code Subscriber1} is notified on {@code message2}</li>
   *  <li>{@code Subscriber2} is notified on {@code message2}</li>
   * </ol>
   * <p>
   * <b>Thread-safety.</b>
   * All subscribers are notified sequentially from the calling thread.
   * <p>
   * <b>Memory management.</b>
   * Returned objects are very light-weight and stateless, so, they are cached by the message bus in 'per-topic' manner.
   * That means that caller of this method is not obliged to keep returned reference along with the reference to the message for
   * further publishing. It's enough to keep reference to the message bus only and publish
   * like {@code messageBus.syncPublisher(targetTopic).targetMethod()}.
   *
   * @param topic target topic
   * @param <L> {@link Topic#getListenerClass() business interface} of the target topic
   * @return publisher for a target topic
   */
  @NotNull
  <L> L syncPublisher(@NotNull Topic<L> topic);

  @Internal
  @NotNull
  <L> L syncAndPreloadPublisher(@NotNull Topic<L> topic);

  /**
   * Disposes current bus, i.e., drops all queued but not delivered messages (if any) and disallows further connections.
   */
  @Override
  void dispose();

  /**
   * Returns true if this bus is disposed.
   */
  boolean isDisposed();

  /**
   * @return true, when events in the given topic are being dispatched in the current thread,
   * and not all listeners have received the events yet.
   */
  boolean hasUndeliveredEvents(@NotNull Topic<?> topic);
}
