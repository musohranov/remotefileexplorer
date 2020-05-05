package com.remotefileexplorer.command;

import java.io.File;

/**
 * Команда "Создать каталог".
 */
final class MkDir extends Command {
    /**
     * @param commandLine Строка с командой.
     */
    MkDir(final String commandLine) throws Exception {
        super(commandLine, true, "Создать директорию. Пример mkdir c:\\test");

        if (!(this.name.equals("mkdir") && this.params.length == 1)) {
            throw new Exception("Не является командой mkdir!");
        }
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     */
    @Override
    public String execute(final File workingDirectory) throws ExecutionError {
        try {
            File directory = new File(workingDirectory.getAbsolutePath() + "/" + this.params[0]);

            if (directory.exists() || !directory.mkdir()) {
                throw new Exception("Ошибка создания директории!");
            }
        } catch (Exception e) {
            throw new ExecutionError(e.getMessage());
        }

        return "Ok";
    }
}
