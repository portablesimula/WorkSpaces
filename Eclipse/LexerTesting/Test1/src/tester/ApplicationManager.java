package tester;

import java.util.List;

/**
 * Provides access to the {@link Application}.
 */
public class ApplicationManager {
  //@ApiStatus.Internal protected static volatile Application ourApplication;

  public static Application getApplication() {
    return ourApplication;
  }

  //@ApiStatus.Internal
  public static void setApplication(@Nullable Application instance) {
    ourApplication = instance;
    for (Runnable cleaner : cleaners) {
      cleaner.run();
    }
  }

  public static void setApplication(Application instance, Disposable parent) {
    Application old = ourApplication;
    Disposer.register(parent, () -> {
      if (old != null) { // to prevent NPEs in threads still running
        setApplication(old);
      }
    });
    setApplication(instance);
  }

  public static void setApplication(
    Application instance,
    Supplier<? extends FileTypeRegistry> fileTypeRegistryGetter,
    Disposable parent
  ) {
    Application old = ourApplication;
    setApplication(instance);
    FileTypeRegistry.setInstanceSupplier(fileTypeRegistryGetter, parent);
    Disposer.register(parent, () -> {
      if (old != null) {
        // to prevent NPEs in threads still running
        setApplication(old);
      }
    });
  }

  private static final List<Runnable> cleaners = ContainerUtil.createLockFreeCopyOnWriteList();

  /**
   * Registers a cleaning operation to be run when the application instance is reset (for example, in tests).
   */
  //@ApiStatus.Internal
  public static void registerCleaner(Runnable cleaner) {
    cleaners.add(cleaner);
  }
}
