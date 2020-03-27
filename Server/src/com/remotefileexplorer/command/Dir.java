package com.remotefileexplorer.command;

import org.json.simple.JSONObject;

import java.io.File;
import java.util.Objects;

/**
 * Команда "Вывести список файлов".
 */
final class Dir extends Command {
    /**
     * @param commandLine Строка с командой.
     */
    Dir(final String commandLine) throws Exception {
        super(commandLine, false);

        if (!(this.name.equals("dir") && this.params.length <= 1)) {
            throw new Exception("Не является командой dir!");
        }
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     */
    @Override
    public IResult execute(final File workingDirectory) {
        JSONObject jsonResult = new JSONObject();

        // Если указана под-директория, выполняем команду в контексте нее, иначе из рабочей
        File directory = this.params.length == 0 ? workingDirectory
                : new File(workingDirectory.getAbsolutePath() + "/" + this.params[0]);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            JSONObject jsonFile = new JSONObject();

            jsonFile.put("type", file.isDirectory() ? "d" : "f");
            jsonFile.put("size", file.isDirectory() ? 0 : file.length());

            jsonResult.put(file.getName(), jsonFile);
        }

        return new IResult() {
            @Override
            public String toString() {
                return jsonResult.toString();
            }
        };
    }
}
