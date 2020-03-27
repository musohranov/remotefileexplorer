package com.remotefileexplorer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Запрос (Транспортный уровень абстракции).
 */
final class Request {
    /**
     * Идентификатор.
     */
    final String id;

    /**
     * Тип.
     */
    final Type type;

    /**
     * Содержимое.
     */
    final String body;

    /**
     * @param request Запрос.
     */
    Request(final String request) throws InvalidatedRequest {
        try {
            JSONObject jsonData = (JSONObject) new JSONParser().parse(request);

            this.id = jsonData.containsKey("id") ? String.valueOf(jsonData.get("id")) : "";
            if (this.id.isEmpty()) {
                throw new InvalidatedRequest(request, "Идентификатор запроса должен быть заполнен!");
            }

            this.type = getType(jsonData.containsKey("type") ? String.valueOf(jsonData.get("type")) : "");
            if (this.type == null) {
                throw new InvalidatedRequest(request, "Не известный тип запроса!");
            }

            this.body = jsonData.containsKey("body") ? String.valueOf(jsonData.get("body")) : "";
            if (this.body.isEmpty()) {
                throw new InvalidatedRequest(request, "Содержимое запроса должно быть заполнено!");
            }

        } catch (ParseException e) {
            throw new InvalidatedRequest(request, String.format("Запрос не является json, %s", e.getMessage()));
        }
    }

    /**
     * Типы запросов.
     */
    enum Type {
        /**
         * Команда
         */
        Command
    }

    /**
     * Получить тип запроса.
     * @param type Строковое представление запроса.
     */
    private static Type getType(final String type) {
        if (type.equals("command")) {
            return Type.Command;
        }

        return null;
    }

    /**
     * Исключение 'Не корректный запрос'.
     */
    static class InvalidatedRequest extends Exception {
        /**
         * Максимальная длинна строки отражения запроса.
         */
        private static final int MAX_REQUEST_LENGTH = 100;

        /**
         * @param request Запрос.
         * @param message Сообщение об ошибке.
         */
        InvalidatedRequest(final String request, final String message) {
            super(String.format("Не корректный запрос '%s', ошибка '%s'",
                    request.length() > MAX_REQUEST_LENGTH ? request.substring(0, MAX_REQUEST_LENGTH) : request,
                    message));
        }
    }
}
