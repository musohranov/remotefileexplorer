package com.remotefileexplorer.command;

import java.io.File;

/**
 * Команда "Удалить каталог".
 */
final class RmDir extends Command {
    /**
     * @param commandLine Строка с командой.
     */
    RmDir(final String commandLine) throws Exception {
        super(commandLine, true);

        if (!(this.name.equals("rmdir") && this.params.length == 1)) {
            throw new Exception("Не является командой rmdir!");
        }
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     */
    @Override
    public IResult execute(final File workingDirectory) throws ExecutionError {
        try {
            File directory = new File(workingDirectory.getAbsolutePath() + "/" + this.params[0]);
            if (!directory.delete()) {
                throw new Exception("Ошибка удаления директории!");
            }
        } catch (Exception e) {
            throw new ExecutionError(e.getMessage());
        }

        return new IResult() {
            @Override
            public String toString() {
                return "";
            }
        };
    }
}
