package com.intellij.openapi.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;

public final class NlsContexts {
    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "attribute.descriptor"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface AttributeDescriptor {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "border.title"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface BorderTitle {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    @NlsContext(
        prefix = "button"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface Button {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "checkbox"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface Checkbox {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "column.name"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface ColumnName {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "command.name"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface Command {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "configurable.name"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface ConfigurableName {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "text"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface DetailedDescription {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
    @NlsContext(
        prefix = "dialog.message"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface DialogMessage {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    @NlsContext(
        prefix = "dialog.title"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface DialogTitle {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "hint.text"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface HintText {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "label"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface Label {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "link.label"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface LinkLabel {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "list.item"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface ListItem {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "notification.content"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface NotificationContent {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "notification.subtitle"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface NotificationSubtitle {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "notification.title"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface NotificationTitle {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "parsing.error"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface ParsingError {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "popup.advertisement"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface PopupAdvertisement {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "popup.content"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface PopupContent {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "popup.title"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface PopupTitle {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "progress.details"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface ProgressDetails {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "progress.text"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface ProgressText {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "progress.title"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface ProgressTitle {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "radio"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface RadioButton {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "separator"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface Separator {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "status.bar.text"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface StatusBarText {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "status.text"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface StatusText {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "system.notification.text"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface SystemNotificationText {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "system.notification.title"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface SystemNotificationTitle {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    @NlsContext(
        prefix = "tab.title"
    )
    @Nls(
        capitalization = Capitalization.Title
    )
    public @interface TabTitle {
    }

    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    @NlsContext(
        prefix = "tooltip"
    )
    @Nls(
        capitalization = Capitalization.Sentence
    )
    public @interface Tooltip {
    }
}
