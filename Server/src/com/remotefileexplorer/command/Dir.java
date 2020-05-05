package com.remotefileexplorer.command;

import org.json.simple.JSONObject;

import java.io.File;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Команда "Вывести список файлов".
 */
final class Dir extends Command {
    /**
     * @param commandLine Строка с командой.
     */
    Dir(final String commandLine) throws Exception {
        super(commandLine, false, "Вывести список файлов директории. Пример dir c:");

        if (!(this.name.equals("dir") && this.params.length <= 1)) {
            throw new Exception("Не является командой dir!");
        }
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     */
    @Override
    public String execute(final File workingDirectory) {
        StringJoiner result = new StringJoiner("\n");

        // Если указана под-директория, выполняем команду в контексте нее, иначе из рабочей
        File directory = this.params.length == 0 ? workingDirectory
                : new File(workingDirectory.getAbsolutePath() + "/" + this.params[0]);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            result.add(String.format("%s %s %s",
                    file.isDirectory() ? "-d-" : "   ",
                    file.getName(),
                    file.isDirectory() ? "" : ", " + file.length()));
        }

        return result.toString();
    }
}
