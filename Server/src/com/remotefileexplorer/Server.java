package com.remotefileexplorer;

import com.remotefileexplorer.command.Command;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 * Web сервер обработки запросов (Транспортный уровень абстракции).
 */
final class Server extends WebSocketServer {
    /**
     * Рабочая директория.
     */
    private final File workingDirectory;

    /**
     * Разрешение на запись.
     */
    private final boolean writePermission;

    /**
     * @param workingDirectory Рабочая директория.
     * @param writePermission Разрешение на запись.
     * @param port Номер порта.
     */
    private Server(final File workingDirectory, final boolean writePermission, final int port) {
        super(new InetSocketAddress(port));

        this.workingDirectory = workingDirectory;
        this.writePermission = writePermission;
    }

    /**
     * Обработка входящих сообщений.
     * @param webSocket Сокет.
     * @param message Сообщение.
     */
    @Override
    public void onMessage(final WebSocket webSocket, final String message) {
        Logger.getGlobal().info(String.format("Обрабатывается запрос '%s'", message));

        Request request = null;
        try {
            request = new Request(message);

            if (request.type == Request.Type.Command) {
                Command command = Command.getCommand(request.body.toLowerCase());

                if (command.isWriteAccess && !this.writePermission) {
                    throw new Exception("Команды на запись не разрешены!");
                }

                String result = command.execute(this.workingDirectory).toString();
                webSocket.send(result);

                Logger.getGlobal().info(String.format("Результат обработки '%s'", result));
            }
        } catch (Exception e) {
            String result = request == null ? Response.createError(e.getMessage())
                    : Response.createError(request, e.getMessage());
            webSocket.send(result);

            Logger.getGlobal().info(String.format("Результат обработки '%s'", result));
        }
    }

    /**
     * Запустить сервер.
     * @param workingDirectory Рабочая директория.
     * @param writePermission Разрешение на запись.
     * @param port Номер порта.
     */
    static void start(final File workingDirectory, final boolean writePermission, final int port)
            throws IOException, InterruptedException {

        Server server = new Server(workingDirectory, writePermission, port);
        server.start();

        Logger.getGlobal().info(
                String.format("Файловый сервер запущен с настройками"
                        + "\nРабочая директория = %s"
                        + "\nРазрешение на запись = %b"
                        + "\nПорт web сервера = %d", workingDirectory, writePermission, port));

        // Если из входящего потока приняли команду выход, останавливаем сервер
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (sysIn.readLine().equals("exit")) {
                server.stop(1000);
                break;
            }
        }
    }

    @Override
    public void onOpen(final WebSocket webSocket, final ClientHandshake clientHandshake) {
    }

    @Override
    public void onClose(final WebSocket webSocket, final int i, final String s, final boolean b) {
    }

    @Override
    public void onError(final WebSocket webSocket, final Exception e) {
    }

    @Override
    public void onStart() {
    }
}
