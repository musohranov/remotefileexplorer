package com.remotefileexplorer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.*;
import com.remotefileexplorer.transport.Server;


/**
 * Сервер.
 */
public final class App {
    private App() {
    }

    /**
     * Запустить файловый web сервер.
     *
     * @param args Параметры запуска:
     *             - Директория файловой системы.
     *             - Уровень доступа. w - разрешено изменение, r - только чтение.
     *             - Порт web сервера.
     */
    public static void main(final String[] args) throws IOException, InterruptedException {
        Settings settings;
        try {
            settings = new Settings(args);
        } catch (ParseException e) {
            Settings.printHelpFormatter();
            return;
        }

        File workingDirectory = new File(settings.workingDirectory);
        if (!workingDirectory.isDirectory()) {
            Settings.printHelpFormatter();
            return;
        }

        Logger.getGlobal().setLevel(Level.FINE);

        if (!workingDirectory.canWrite() && settings.writePermission) {
            Logger.getGlobal().warning("К рабочей директории нет доступа на запись!");
            return;
        }

        Server.start(workingDirectory, settings.writePermission, settings.port);
    }
}

/**
 * Настройки.
 */
class Settings {
    /**
     * Рабочая директория.
     */
    final String workingDirectory;
    private static final String WORKING_DIRECTORY_OPTION = "d";

    /**
     * Разрешение на запись. Поумолчанию не разрешено.
     */
    final boolean writePermission;
    private static final String WRITE_PERMISSION_OPTION = "w";

    /**
     * Порт web сервера.
     */
    final int port;
    private static final String PORT_OPTION = "p";

    /**
     * @param args Входные аргументы командной строки приложения.
     */
    Settings(final String[] args) throws ParseException {
        CommandLineParser parser = new BasicParser();
        CommandLine line = parser.parse(Settings.getOptions(), args);

        this.workingDirectory = line.getOptionValue(WORKING_DIRECTORY_OPTION);
        this.writePermission = line.hasOption(WRITE_PERMISSION_OPTION);
        this.port = Integer.parseInt(line.getOptionValue(PORT_OPTION));
    }

    /**
     * Перечень опций парсера командной строки.
     */
    private static Options options = null;

    /**
     * Получить декларацию опций парсера командной строки.
     */
    private static Options getOptions() {
        if (Settings.options == null) {
            Settings.options = new Options();

            OptionBuilder.hasArg().withArgName("Директория").isRequired()
                    .withDescription("Рабочая директория относительно которой происходит выполнения команд");
            Settings.options.addOption(OptionBuilder.create(WORKING_DIRECTORY_OPTION));

            OptionBuilder.withDescription("Разрешение на запись");
            Settings.options.addOption(OptionBuilder.create(WRITE_PERMISSION_OPTION));

            OptionBuilder.hasArg().withArgName("Порт").isRequired()
                    .withDescription("Порт web сервера");
            Settings.options.addOption(OptionBuilder.create(PORT_OPTION));
        }

        return Settings.options;
    }

    /**
     * Получить хелп по формату командной строки.
     */
    static void printHelpFormatter() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.defaultSyntaxPrefix = "Использование: ";

        helpFormatter.printHelp("RemoteFileExplorer.jar", "", options, "", true);
    }
}
