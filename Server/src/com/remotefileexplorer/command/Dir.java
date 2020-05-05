package com.remotefileexplorer.command;

import java.io.File;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Команда "Вывести список файлов".
 */
final class Dir extends Command {
    /**
     *
     */
    Dir() {
        super("dir", false, "Вывести список файлов директории. Пример dir c:\\ ");
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     * @param params Параметры.
     */
    @Override
    String execute(File workingDirectory, String[] params) throws Exception {
        StringJoiner result = new StringJoiner("\n");

        if (params.length > 1) {
            throw new Exception("Параметры команды заданы не верно!");
        }

        // Если указана под-директория, выполняем команду в контексте нее, иначе из рабочей
        File directory = params.length == 0 ? workingDirectory
                : new File(workingDirectory.getAbsolutePath() + "/" + params[0]);

        if (!directory.isDirectory()) {
            throw new Exception("Директория указана не верно!");
        }

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            // Сформировать строку результата,
            // для файла        :    file_name, size
            // для директории   :-d- dir_name
            result.add(String.format("%s %s%s",
                    file.isDirectory() ? "-d-" : "   ",
                    file.getName(),
                    file.isDirectory() ? "" : ", " + file.length()));
        }

        return result.toString();
    }
}
