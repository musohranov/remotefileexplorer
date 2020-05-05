package com.remotefileexplorer.command;

import java.io.File;

/**
 * Команда.
 */
abstract class Command {
    /**
     * Наименование команды.
     */
    final String name;

    /**
     * Признак команды уровня изменения данных.
     */
    final boolean isWriteAccess;

    /**
     * Описание.
     */
    final String description;

    /**
     * @param name Наименование команды.
     * @param description Описание.
     */
    protected Command(final String name, boolean isWriteAccess, final String description) {
        this.name = name;
        this.description = description;
        this.isWriteAccess = isWriteAccess;
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     * @param params Параметры.
     */
    abstract String execute(final File workingDirectory, final String[] params) throws Exception;

    /**
     * Успешное выполнение команды.
     */
    protected final String SUCCESS = "Ok";
}
