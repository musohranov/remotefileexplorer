package com.remotefileexplorer.command;

import java.io.File;

/**
 * Команда "Удалить директорию".
 */
final class RmDir extends Command {
    /**
     *
     */
    RmDir() {
        super("rmdir", true, "Удалить директорию. Например rmdir c:\\test");
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

        File directory = new File(workingDirectory.getAbsolutePath() + "/" + params[0]);

        if (!directory.isDirectory()) {
            throw new Exception("Директория указана не верно!");
        }

        if (!directory.delete()) {
            throw new Exception("Ошибка удаления директории!");
        }

        return this.SUCCESS;
    }
}
