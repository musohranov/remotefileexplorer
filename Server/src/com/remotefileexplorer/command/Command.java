package com.remotefileexplorer.command;

import java.io.File;
import java.util.Arrays;

/**
 * Команда. Формат строки 'cmd [params]'
 */
public abstract class Command {
    /**
     * Наименование команды.
     */
    final String name;

    /**
     * Параметры.
     */
    final String[] params;

    /**
     * Признак команды уровня изменения данных.
     */
    public final boolean isWriteAccess;

    /**
     * @param commandLine Строка команды.
     * @param isWriteAccess Признак команды уровня изменения данных.
     */
    protected Command(final String commandLine, final boolean isWriteAccess) {
        String[] splited = commandLine.trim().split(" ");

        this.name = splited[0];
        this.params = splited.length > 1 ? Arrays.copyOfRange(splited, 1, splited.length) : new String[]{};
        this.isWriteAccess = isWriteAccess;
    }

    /**
     * Получить команду.
     * @param commandLine Строка команды.
     */
    public static Command getCommand(final String commandLine) throws Exception {
        try {
            return new Dir(commandLine);
        } catch (Exception ignored) {}

        try {
            return new MkDir(commandLine);
        } catch (Exception ignored) {}

        try {
            return new RmDir(commandLine);
        } catch (Exception ignored) {}

        throw new Exception("Не известная команда!");
    }

    /**
     * Результат выполнения команды.
     */
    public interface IResult {
        /**
         * Привести результат к строке.
         */
        String toString();
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     */
    public abstract IResult execute(File workingDirectory) throws ExecutionError;

    /**
     * Исключение 'Ошибка выполнения команды'.
     */
    public static class ExecutionError extends Exception {
        ExecutionError(final String message) {
            super(message);
        }
    }
}
