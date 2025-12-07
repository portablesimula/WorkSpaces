package tester;

public abstract class ProgressManager extends ProgressIndicatorProvider {
	  private static ProgressManager ourInstance;

	  @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
	  public static ProgressManager getInstance() {
	    ProgressManager instance = ourInstance;
	    if (instance == null) {
	      instance = ApplicationManager.getApplication().getService(ProgressManager.class);
	      ourInstance = instance;
	    }
	    return instance;
	  }

	  /**
	   * @return ProgressManager or null if not yet initialized
	   */
	  //@ApiStatus.Internal
	  public static ProgressManager getInstanceOrNull() {
	    return ourInstance;
	  }

	  public abstract boolean hasProgressIndicator();
	  public abstract boolean hasModalProgressIndicator();
	  public abstract boolean hasUnsafeProgressIndicator();

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * <ul>
	   * <li>
	   *   If only {@link ProgressManager#checkCanceled} is supposed to work inside {@code process},
	   *   use {@link CoroutinesKt#blockingContext} in a coroutine.
	   * </li>
	   * <li>
	   *   If {@link ProgressManager#getProgressIndicator} is expected to return a non-null value,
	   *   use {@link CoroutinesKt#coroutineToIndicator} in a coroutine.
	   * </li>
	   * </ul>
	   * </p>
	   * <hr>
	   *
	   * Runs the given process synchronously in calling thread, associating this thread with the specified progress indicator.
	   * This means that it'll be returned by {@link ProgressManager#getProgressIndicator()} inside the {@code process},
	   * and {@link ProgressManager#checkCanceled()} will throw a {@link ProcessCanceledException} if the progress indicator is canceled.
	   *
	   * @param progress an indicator to use, {@code null} means reuse current progress.
	   *                 The progress is {@link ProgressIndicator#start started} before running {@code process} and {@link ProgressIndicator#stop() stopped} afterward.
	   */
	  //@Obsolete
	  public abstract void runProcess(Runnable process, ProgressIndicator progress) throws ProcessCanceledException;

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * See {@link #runProcess(Runnable, ProgressIndicator)} notice.
	   * </p>
	   * <hr>
	   *
	   * Performs the given computation synchronously in calling thread and returns its result, associating this thread with the specified progress indicator.
	   * This means that it'll be returned by {@link ProgressManager#getProgressIndicator()} inside the {@code process},
	   * and {@link ProgressManager#checkCanceled()} will throw a {@link ProcessCanceledException} if the progress indicator is canceled.
	   *
	   * @param progress an indicator to use, {@code null} means reuse current progress
	   *                 The progress is {@link ProgressIndicator#start started} before running {@code process} and {@link ProgressIndicator#stop() stopped} afterward.
	   *
	   * @see CoroutinesKt#coroutineToIndicator
	   */
	  //@Obsolete
	  public final <T> T runProcess(Computable<T> process, ProgressIndicator progress) throws ProcessCanceledException {
	    Ref<T> ref = new Ref<>();
	    runProcess(() -> ref.set(process.compute()), progress);
	    return ref.get();
	  }

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * <ul>
	   * <li>
	   *   If the returned indicator is used only for {@link ProgressIndicator#checkCanceled()},
	   *   use {@link ProgressManager#checkCanceled()} directly.
	   * </li>
	   * <li>
	   *   If the returned indicator is used to report progress, use coroutines and their progress reporting capabilities:
	   *   {@link StepsKt#reportProgress}, {@link StepsKt#reportSequentialProgress}, {@link StepsKt#reportRawProgress}.
	   * </li>
	   * <li>
	   *   If the returned indicator is used to create an indicator wrapper,
	   *   which is {@link ProgressManager#runProcess(Runnable, ProgressIndicator) installed} in another thread,
	   *   migrate to coroutines, and use one of approaches described in {@link ProgressManager#runProcess(Runnable, ProgressIndicator)}.
	   * </li>
	   * </ul>
	   * </p>
	   */
	  //@Obsolete
	  @Override
	  public abstract ProgressIndicator getProgressIndicator();

	  public static void progress(String text) throws ProcessCanceledException {
	    progress(text, "");
	  }

	  public static void progress2(String text) throws ProcessCanceledException {
	    final ProgressIndicator pi = getInstance().getProgressIndicator();
	    if (pi != null) {
	      pi.checkCanceled();
	      pi.setText2(text);
	    }
	  }

	  public static void progress(String text,
	                              String text2) throws ProcessCanceledException {
	    final ProgressIndicator pi = getInstance().getProgressIndicator();
	    if (pi != null) {
	      pi.checkCanceled();
	      pi.setText(text);
	      pi.setText2(text2 == null ? "" : text2);
	    }
	  }

	  /**
	   * Runs the specified operation in non-cancellable manner synchronously on the same thread where it was called.
	   *
	   * @see ProgressManager#computeInNonCancelableSection(ThrowableComputable)
	   * @param runnable the operation to execute
	   */
	  public abstract void executeNonCancelableSection(Runnable runnable);

	  /**
	   * Runs the specified operation and return its result in non-cancellable manner synchronously on the same thread where it was called.
	   *
	   * @see ProgressManager#executeNonCancelableSection(Runnable)
	   * @param computable the operation to execute
	   */
	  public abstract <T, E extends Exception> T computeInNonCancelableSection(ThrowableComputable<T, E> computable) throws E;

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * <ul>
	   * <li>Consider getting rid of a modal progress altogether, for example, by using a background progress,</li>
	   * <li>or use {@link com.intellij.platform.ide.progress.TasksKt#runWithModalProgressBlocking}</li>
	   * <li>or {@link com.intellij.platform.ide.progress.TasksKt#withModalProgress}.</li>
	   * </ul>
	   * </p>
	   * <hr>
	   *
	   * Runs the specified operation in a background thread and shows a modal progress dialog in the
	   * main thread while the operation is executing.
	   * If a dialog can't be shown (e.g. under write action or in headless environment),
	   * runs the given operation synchronously in the calling thread.
	   *
	   * @param process       the operation to execute.
	   * @param progressTitle the title of the progress window.
	   * @param canBeCanceled whether "Cancel" button is shown on the progress window.
	   * @param project       the project in the context of which the operation is executed.
	   * @return true if the operation completed successfully, false if it was cancelled.
	   */
	  //@Obsolete
	  public abstract boolean runProcessWithProgressSynchronously(Runnable process,
	                                                              String progressTitle,
	                                                              boolean canBeCanceled,
	                                                              Project project);

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * See {@link #runProcessWithProgressSynchronously(Runnable, String, boolean, Project)} notice.
	   * </p>
	   * <hr>
	   *
	   * Runs the specified operation in a background thread and shows a modal progress dialog in the
	   * main thread while the operation is executing.
	   * If a dialog can't be shown (e.g. under write action or in headless environment),
	   * runs the given operation synchronously in the calling thread.
	   *
	   * @param process       the operation to execute.
	   * @param progressTitle the title of the progress window.
	   * @param canBeCanceled whether "Cancel" button is shown on the progress window.
	   * @param project       the project in the context of which the operation is executed.
	   * @return true result of operation
	   * @throws E exception thrown by process
	   */
	  //@Obsolete
	  public abstract <T, E extends Exception> T runProcessWithProgressSynchronously(ThrowableComputable<T, E> process,
	                                                                                 String progressTitle,
	                                                                                 boolean canBeCanceled,
	                                                                                 Project project) throws E;

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * See {@link #runProcessWithProgressSynchronously(Runnable, String, boolean, Project)} notice.
	   * </p>
	   * <hr>
	   *
	   * Runs the specified operation in a background thread and shows a modal progress dialog in the
	   * main thread while the operation is executing.
	   * If a dialog can't be shown (e.g. under write action or in headless environment),
	   * runs the given operation synchronously in the calling thread.
	   *
	   * @param process         the operation to execute.
	   * @param progressTitle   the title of the progress window.
	   * @param canBeCanceled   whether "Cancel" button is shown on the progress window.
	   * @param project         the project in the context of which the operation is executed.
	   * @param parentComponent the component which will be used to calculate the progress window ancestor
	   * @return true if the operation completed successfully, false if it was cancelled.
	   */
	  //@Obsolete
	  public abstract boolean runProcessWithProgressSynchronously(Runnable process,
	                                                              String progressTitle,
	                                                              boolean canBeCanceled,
	                                                              Project project,
	                                                              JComponent parentComponent);

	  /**
	   * Runs a specified {@code process} in a background thread and shows a progress dialog, which can be made non-modal by pressing
	   * background button. Upon successful termination of the process a {@code successRunnable} will be called in Swing UI thread and
	   * {@code canceledRunnable} will be called if terminated on behalf of the user by pressing either cancel button, while running in
	   * a modal state or stop button if running in background.
	   *
	   * @param project          the project in the context of which the operation is executed.
	   * @param progressTitle    the title of the progress window.
	   * @param process          the operation to execute.
	   * @param successRunnable  a callback to be called in Swing UI thread upon normal termination of the process.
	   * @param canceledRunnable a callback to be called in Swing UI thread if the process have been canceled by the user.
	   * @param option           progress indicator behavior controller.
	   * @deprecated use {@link #run(Task)}
	   */
	  @Deprecated
	  //@ApiStatus.ScheduledForRemoval
	  public abstract void runProcessWithProgressAsynchronously(Project project,
	                                                            String progressTitle,
	                                                            Runnable process,
	                                                            Runnable successRunnable,
	                                                            Runnable canceledRunnable,
	                                                            PerformInBackgroundOption option);

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * <br/>
	   * Do not use directly!
	   * Use {@link Task#queue()} instead of this method, or migrate the Task to coroutines.
	   * Please find appropriate replacements in the respective Task documentation:
	   * {@link Task.Backgroundable}, {@link Task.Modal}, {@link Task.WithResult}, {@link Task.ConditionalModal}.
	   * </p>
	   * <hr>
	   *
	   * Runs a specified {@code task} in either background/foreground thread and shows a progress dialog.
	   *
	   * @param task task to run (either {@link Task.Modal} or {@link Task.Backgroundable}).
	   *
	   * @see com.intellij.openapi.progress.TasksKt#withBackgroundProgress
	   * @see com.intellij.openapi.progress.TasksKt#withModalProgress
	   * @see com.intellij.openapi.progress.TasksKt#runWithModalProgressBlocking
	   */
	  //@Obsolete
	  //@RequiresBlockingContext
	  public abstract void run(Task task);

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * See {@link Task.WithResult} notice.
	   * </p>
	   * <hr>
	   *
	   * Runs a specified computation with a modal progress dialog.
	   */
	  //@Obsolete
	  //@RequiresBlockingContext
	  public <T, E extends Exception> T run(Task.WithResult<T, E> task) throws E {
	    run((Task)task);
	    return task.getResult();
	  }

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * See {@link ProgressManager#run(Task)} notice.
	   * </p>
	   */
	  //@Obsolete
	  public abstract void runProcessWithProgressAsynchronously(Task.Backgroundable task, ProgressIndicator progressIndicator);

	  protected void indicatorCanceled(ProgressIndicator indicator) { }

	  public static void canceled(ProgressIndicator indicator) {
	    getInstance().indicatorCanceled(indicator);
	  }

	  /**
	   * @throws ProcessCanceledException if the progress indicator associated with the current thread has been canceled.
	   * @see ProgressIndicator#checkCanceled()
	   */
	  @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
	  //@RequiresBlockingContext
	  public static void checkCanceled() throws ProcessCanceledException {
	    ProgressManager instance = getInstanceOrNull();
	    if (instance != null) {
	      instance.doCheckCanceled();
	    }
	  }

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * See {@link #runProcess(Runnable, ProgressIndicator)} notice.
	   * </p>
	   * <hr>
	   *
	   * @param progress an indicator to use, {@code null} means reuse current progress
	   *        The methods {@link ProgressIndicator#start()} or {@link ProgressIndicator#stop()} are not called because it's assumed the {@code progress} is already running.
	   */
	  //@Obsolete
	  public abstract void executeProcessUnderProgress(Runnable process, ProgressIndicator progress) throws ProcessCanceledException;

	  public static void assertNotCircular(ProgressIndicator original) {
	    Set<ProgressIndicator> wrappedParents = null;
	    for (ProgressIndicator i = original; i instanceof WrappedProgressIndicator; i = ((WrappedProgressIndicator)i).getOriginalProgressIndicator()) {
	      if (wrappedParents == null) {
	        wrappedParents = new HashSet<>();
	      }
	      if (!wrappedParents.add(i)) {
	        throw new IllegalArgumentException(i + " wraps itself");
	      }
	    }
	  }

	  /**
	   * <h3>Obsolescence notice</h3>
	   * <p>
	   * See {@link ProgressIndicator} notice.
	   * Use {@link com.intellij.openapi.application.ReadAction#computeCancellable} instead.
	   * </p>
	   * <hr>
	   *
	   * This method attempts to run provided action synchronously in a read action, so that, if possible, it wouldn't impact any pending,
	   * executing or future write actions (for this to work effectively the action should invoke {@link ProgressManager#checkCanceled()} or
	   * {@link ProgressIndicator#checkCanceled()} often enough).
	   * It returns {@code true} if action was executed successfully. It returns {@code false} if the action was not
	   * executed successfully, i.e. if:
	   * <ul>
	   * <li>write action was in progress when the method was called</li>
	   * <li>write action was pending when the method was called</li>
	   * <li>action started to execute, but was aborted using {@link ProcessCanceledException} when some other thread initiated
	   * write action</li>
	   * </ul>
	   * @param process the code to execute under read action
	   * @param indicator progress indicator that should be cancelled if a write action is about to start. Can be null.
	   *                 The progress is {@link ProgressIndicator#start started} before running {@code process} and {@link ProgressIndicator#stop() stopped} afterward.
	   */
	  //@Obsolete
	  public abstract boolean runInReadActionWithWriteActionPriority(final Runnable process, ProgressIndicator indicator);

	  //@RequiresBlockingContext
	  public abstract boolean isInNonCancelableSection();

	  /**
	   * Performs the given computation while giving more priority to the current thread
	   * (by forcing all other non-prioritized threads to sleep a bit whenever they call {@link #checkCanceled()}).<p></p>
	   *
	   * This is intended for relatively short (expected to be under 10 seconds) background activities that the user is waiting for
	   * (e.g. code navigation), and which shouldn't be slowed down by CPU-intensive background tasks like highlighting or indexing.
	   */
	  //@ApiStatus.Internal
	  public abstract <T, E extends Throwable> T computePrioritized(ThrowableComputable<T, E> computable) throws E;

	  /**
	   * Makes {@link #getProgressIndicator()} return {@code null} within {@code computable}.
	   */
	  //@ApiStatus.Internal
	  public abstract <X> X silenceGlobalIndicator(Supplier<? extends X> computable);

	  /**
	   * Don't call this method directly!
	   * Use {@link ModalityState#defaultModalityState()} instead.
	   */
	  //@Obsolete
	  //@ApiStatus.Internal
	  public abstract ModalityState getCurrentProgressModality();

	  static {
	    ApplicationManager.registerCleaner(() -> ourInstance = null);
	  }
	}
