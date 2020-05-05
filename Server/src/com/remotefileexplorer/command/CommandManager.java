package com.remotefileexplorer.command;

import java.io.File;
import java.util.*;

/**
 * Менджер команд.
 */
public class CommandManager {
    /**
     * Экземпляр менеджера команд.
     */
    private static CommandManager commandManager = null;

    /**
     * Получить экземпляр менеджера команд.
     */
    public static CommandManager getInstance() {
        if (commandManager == null) {
            commandManager = new CommandManager();
        }

        return commandManager;
    };

    /**
     * Список доступных команд.
     */
    private final HashMap<String, Command> commandList = new HashMap<>();

    /**
     *
     */
    private CommandManager() {
        Help help = new Help();
        commandList.put(help.name, help);

        Dir dir = new Dir();
        commandList.put(dir.name, dir);

        MkDir mkdir = new MkDir();
        commandList.put(mkdir.name, mkdir);

        RmDir rmdir = new RmDir();
        commandList.put(rmdir.name, rmdir);
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     */
    public String execute(final File workingDirectory,
                          final boolean isWriteAccess,
                          final String commandLine)
            throws Exception {

        String[] wordList = commandLine.trim().split(" ");

        final String commandName = wordList[0].toLowerCase();
        final String[] commandParams =
                wordList.length > 1 ? Arrays.copyOfRange(wordList, 1, wordList.length) : new String[]{};

        Command command = this.commandList.get(commandName);
        if (command == null) {
            throw new Exception("Не известная команда! Воспользуйтесь командой help");
        }

        if (!isWriteAccess && command.isWriteAccess) {
            throw new Exception("Команды на запись не разрешены!");
        }

        return command.execute(workingDirectory, commandParams);
    }

    /**
     * Команда "Вывести список доступны команд".
     */
    private static class Help extends Command {
        Help() {
            super("help", false, "Укажите имя команды для получения помощи только по ней. Например help dir");
        }

        @Override
        String execute(File workingDirectory, String[] params) throws Exception {
            if (params.length > 1) {
                throw new Exception("Параметры команды заданы не верно!");
            }

            if (params.length == 0) {
                StringJoiner result = new StringJoiner("\n");

                for (Command command : CommandManager.getInstance().commandList.values()) {
                    result.add(command.name + ", " + command.description);
                }

                return result.toString();
            }

            Command command = CommandManager.getInstance().commandList.get(params[0]);
            if (command != null) {
                return command.description;
            }

            return String.format("Неизвестная команда '%s'", params[0]);
        }
    }
}