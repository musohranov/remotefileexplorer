package com.remotefileexplorer;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервер.
 */
public final class App {
    private App() {}

    /**
     * Запустить файловый web сервер.
     *
     * @param args Параметры запуска:
     * - Каталог файловой системы.
     * - Уровень доступа. w - разрешено изменение, r - только чтение.
     * - Порт web сервера.
     */
    public static void main(final String[] args) throws IOException, InterruptedException {
        Logger.getGlobal().setLevel(Level.FINE);

        Settings settings;

        try {
            settings = new Settings(args);
        } catch (ParseException e) {
            Logger.getGlobal().warning("Не корректная установка настроек! " + e.toString());
            return;
        }

        File workingDirectory = new File(settings.workingDirectory);
        if (!workingDirectory.canWrite() && settings.writePermission) {
            Logger.getGlobal().warning("К рабочей директории нет доступа на запись!");
            return;
        }

        Server.start(workingDirectory, settings.writePermission, settings.port);
    }

    /**
     * Настройки.
     */
    private static class Settings {
        /**
         * Рабочая директория.
         */
        final String workingDirectory;

        /**
         * Разрешение на запись. Поумолчанию не разрешено.
         */
        final boolean writePermission;

        /**
         * Порт web сервера. По умолчанию 8080.
         */
        final int port;

        /**
         * @param args Входные аргументы
         */
        Settings(final String[] args) throws ParseException {
            Options options = new Options();

            options.addOption("f", true, "Рабочая дирнектория");
            options.addOption("w", false, "Разрешение на запись");
            options.addOption("p", true, "Порт web сервера");

            CommandLineParser parser = new BasicParser();
            CommandLine line = parser.parse(options, args);

            this.workingDirectory = line.getOptionValue("f");
            this.writePermission = line.hasOption("w");

            int port = 8080;
            try {
                port = Integer.parseInt(line.getOptionValue("p"));
            } catch (NumberFormatException ignored) {}
            this.port = port;
        }
    }
}

