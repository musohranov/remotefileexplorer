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
     * Описание.
     */
    final String description;

    /**
     * Признак команды уровня изменения данных.
     */
    public final boolean isWriteAccess;

    /**
     * @param commandLine Строка команды.
     * @param isWriteAccess Признак команды уровня изменения данных.
     */
    protected Command(final String commandLine, final boolean isWriteAccess, final String description) {
        String[] splited = commandLine.trim().split(" ");

        this.name = splited[0];
        this.params = splited.length > 1 ? Arrays.copyOfRange(splited, 1, splited.length) : new String[]{};
        this.isWriteAccess = isWriteAccess;
        this.description = description;
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

        try {
            return new Help(commandLine);
        } catch (Exception ignored) {}

        throw new Exception("Не известная команда! Воспользуйтесь командой help");
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     */
    public abstract String execute(File workingDirectory) throws ExecutionError;

    /**
     * Исключение 'Ошибка выполнения команды'.
     */
    static class ExecutionError extends Exception {
        ExecutionError(final String message) {
            super(message);
        }
    }

    /**
     * Команда "Вывести помощь по спискам команд".
     */
    private static class Help extends Command {
        /**
         * @param commandLine Строка с командой.
         */
        Help(String commandLine) throws Exception {
            super(commandLine, false, "Вывести помощь по спискам команд");

            if (!(this.name.equals("help") && this.params.length <= 1)) {
                throw new Exception("Не является командой help!");
            }
        }

        /**
         * Выполнить команду.
         * @param workingDirectory Рабочая директория.
         */
        @Override
        public String execute(File workingDirectory) {
            return "dir\nmkdir\nrmdir";
        }
    }
}