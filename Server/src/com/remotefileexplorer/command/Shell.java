package com.remotefileexplorer.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

/**
 * Команда "Выполнить shell команду".
 */
final class Shell extends Command {
    /**
     *
     */
    Shell() {
        super("sh", true, "Выполнить shell команду. Например sh dir");
    }

    /**
     * Выполнить команду.
     * @param workingDirectory Рабочая директория.
     * @param params Параметры.
     */
    @Override
    String execute(File workingDirectory, String[] params) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(workingDirectory);

        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            builder.command("cmd.exe", "/c", String.join(" ", params));
        } else {
            builder.command("sh", "-c", String.join(" ", params));
        }

        Process process = builder.start();
        BufferedReader resultReader = new BufferedReader(new InputStreamReader(
                process.getInputStream(),
                isWindows ? "cp866" : Charset.defaultCharset().name()
        ));
        String result = resultReader.lines().collect(Collectors.joining("\n"));

        if (process.waitFor() != 0) {
            throw new Exception("Ошибка выполнения команды");
        }

        return result;
    }
}
