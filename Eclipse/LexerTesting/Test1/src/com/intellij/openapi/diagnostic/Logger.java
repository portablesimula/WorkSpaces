package com.intellij.openapi.diagnostic;

import com.intellij.util.ArrayUtilRt;
import com.intellij.util.ExceptionUtil;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.concurrent.CancellationException;
import java.util.function.Function;
import org.apache.log4j.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

public abstract class Logger {
    private static boolean isUnitTestMode;
    private static Factory ourFactory = new DefaultFactory();
    static final Function<Attachment, String> ATTACHMENT_TO_STRING = (attachment) -> attachment.getPath() + "\n" + attachment.getDisplayText();
    private static final boolean ourRethrowCE = "true".equals(System.getProperty("idea.log.rethrow.ce", "true"));

    public static void setFactory(@NotNull Class<? extends Factory> factory) {
        if (isInitialized()) {
            if (factory.isInstance(ourFactory)) {
                return;
            }

            logFactoryChanged(factory);
        }

        try {
            Constructor<? extends Factory> constructor = factory.getDeclaredConstructor();
            constructor.setAccessible(true);
            ourFactory = (Factory)constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void setFactory(@NotNull Factory factory) {
        if (isInitialized()) {
            logFactoryChanged(factory.getClass());
        }

        ourFactory = factory;
    }

    private static void logFactoryChanged(Class<? extends Factory> factory) {
        if (Boolean.getBoolean("idea.log.logger.factory.changed")) {
            System.out.println("Changing log factory from " + ourFactory.getClass().getCanonicalName() + " to " + factory.getCanonicalName() + '\n' + ExceptionUtil.getThrowableText(new Throwable()));
        }

    }

    public static Factory getFactory() {
        return ourFactory;
    }

    public static boolean isInitialized() {
        return !(ourFactory instanceof DefaultFactory);
    }

    public static @NotNull Logger getInstance(@NotNull String category) {
        return ourFactory.getLoggerInstance(category);
    }

//    public static @NotNull Logger getInstance(@NotNull Class<?> cl) {
//        return ourFactory.getLoggerInstance("#" + cl.getName());
//    }

    public abstract boolean isDebugEnabled();

    public void debug(String message) {
        this.debug(message, (Throwable)null);
    }

    public void debug(@Nullable Throwable t) {
        if (t != null) {
            this.debug(t.getMessage(), t);
        }

    }

    public abstract void debug(String var1, @Nullable Throwable var2);

    public void debug(@NotNull String message, @NotNull Object... details) {
        if (details == null) {
            //$$$reportNull$$$0(7);
        }

        if (this.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(message);

            for(Object detail : details) {
                sb.append(detail);
            }

            this.debug(sb.toString());
        }

    }

    public void debugValues(@NotNull String header, @NotNull Collection<?> values) {
        if (this.isDebugEnabled()) {
            StringBuilder text = new StringBuilder();
            text.append(header).append(" (").append(values.size()).append(")");
            if (!values.isEmpty()) {
                text.append(":");

                for(Object value : values) {
                    text.append("\n");
                    text.append(value);
                }
            }

            this.debug(text.toString());
        }

    }

    public final void infoWithDebug(@NotNull Throwable t) {
        this.infoWithDebug(t.toString(), t);
    }

    public final void infoWithDebug(@NotNull String message, @NotNull Throwable t) {
        this.info(message);
        this.debug(t);
    }

    public final void warnWithDebug(@NotNull Throwable t) {
        this.warnWithDebug(t.toString(), t);
    }

    public final void warnWithDebug(@NotNull String message, @NotNull Throwable t) {
        this.warn(message);
        this.debug(t);
    }

    public boolean isTraceEnabled() {
        return this.isDebugEnabled();
    }

    public void trace(String message) {
        this.debug(message);
    }

    public void trace(@Nullable Throwable t) {
        this.debug(t);
    }

    public void info(@NotNull Throwable t) {
        this.info(t.getMessage(), t);
    }

    public void info(String message) {
        this.info(message, (Throwable)null);
    }

    public abstract void info(String var1, @Nullable Throwable var2);

    public void warn(String message) {
        this.warn(message, (Throwable)null);
    }

    public void warn(@NotNull Throwable t) {
        this.warn(t.getMessage(), t);
    }

    public abstract void warn(String var1, @Nullable Throwable var2);

    public void error(String message) {
        this.error(message, new Throwable(message), ArrayUtilRt.EMPTY_STRING_ARRAY);
    }

    /** @deprecated */
    @Deprecated
    public void error(Object message) {
        this.error(String.valueOf(message));
    }

    public void error(String message, @NotNull Attachment... attachments) {
        if (attachments == null) {
            $$$reportNull$$$0(18);
        }

        this.error(message, (Throwable)null, (Attachment[])attachments);
    }

    public void error(String message, @Nullable Throwable t, @NotNull Attachment... attachments) {
        if (attachments == null) {
            $$$reportNull$$$0(19);
        }

        String[] result = new String[attachments.length];

        for(int i = 0; i < attachments.length; ++i) {
            result[i] = (String)ATTACHMENT_TO_STRING.apply(attachments[i]);
        }

        this.error(message, t, result);
    }

    public void error(String message, @NotNull String... details) {
        if (details == null) {
            $$$reportNull$$$0(20);
        }

        this.error(message, new Throwable(message), details);
    }

    public void error(String message, @Nullable Throwable t) {
        this.error(message, t, ArrayUtilRt.EMPTY_STRING_ARRAY);
    }

    public void error(@NotNull Throwable t) {
        this.error(t.getMessage(), t, ArrayUtilRt.EMPTY_STRING_ARRAY);
    }

    public abstract void error(String var1, @Nullable Throwable var2, @NotNull String... var3);

    @Contract("false,_->fail")
    public boolean assertTrue(boolean value, @Nullable Object message) {
        if (!value) {
            String resultMessage = "Assertion failed";
            if (message != null) {
                resultMessage = resultMessage + ": " + message;
            }

            this.error(resultMessage, new Throwable(resultMessage));
        }

        return value;
    }

    @Contract("false->fail")
    public boolean assertTrue(boolean value) {
        return value || this.assertTrue(false, (Object)null);
    }

    /** @deprecated */
    @Deprecated
    public void setLevel(@NotNull Level level) {
        this.error("Do not use, call '#setLevel(LogLevel)' instead");
    }

    public void setLevel(@NotNull LogLevel level) {
        this.error(this.getClass() + " should override '#setLevel(LogLevel)'");
    }

    public static boolean shouldRethrow(@NotNull Throwable t) {
        return t instanceof ControlFlowException || t instanceof CancellationException && ourRethrowCE;
    }

    protected static @Nullable Throwable ensureNotControlFlow(@Nullable Throwable t) {
        return t != null && shouldRethrow(t) ? new Throwable("Control-flow exceptions (e.g. this " + t.getClass() + ") should never be logged. Instead, these should have been rethrown if caught.", t) : t;
    }

    @TestOnly
    public static void setUnitTestMode() {
        isUnitTestMode = true;
    }

    public void warnInProduction(@NotNull Throwable t) {
        if (isUnitTestMode) {
            this.error(t);
        } else {
            this.warn(t);
        }

    }

    private static final class DefaultFactory implements Factory {
        private DefaultFactory() {
        }

        public @NotNull Logger getLoggerInstance(@NotNull String category) {
            return new DefaultLogger(category);
        }
    }

    public interface Factory {
        @NotNull Logger getLoggerInstance(@NotNull String var1);
    }
}
