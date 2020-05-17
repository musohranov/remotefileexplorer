package com.remotefileexplorer.command;

import java.io.File;

/**
 * Команда "Создать каталог".
 */
final class MkDir extends Command {
    /**
     *
     */
    MkDir() {
        super("mkdir", true, "Создать директорию. Например mkdir test");
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     * @param params Параметры.
     */
    @Override
    String execute(File workingDirectory, String[] params) throws Exception {
        if (params.length != 1) {
            throw new Exception("Параметры команды заданы не верно!");
        }

        File directory = new File(workingDirectory, params[0]);

        if (directory.exists() || !directory.mkdir()) {
            throw new Exception("Ошибка создания директории!");
        }

        return this.SUCCESS;
    }
}
