package com.remotefileexplorer;

import org.json.simple.JSONObject;

/**
 * Ответ (Транспортный уровень абстракции).
 */
final class Response {
    private Response() {}

    /**
     * Создать ответ с типом "Ошибка".
     * @param message Сообщение об ошибке.
     */
    static String createError(final String message) {
        JSONObject jsonData = new JSONObject();

        jsonData.put("result_type", "error");
        jsonData.put("result_body", message);

        return jsonData.toString();
    }

    /**
     * Создать ответ с типом "Ошибка".
     * @param request Запрос.
     * @param message Сообщение об ошибке.
     */
    static String createError(final Request request, final String message) {
        JSONObject jsonData = new JSONObject();

        jsonData.put("request_id", request.id);
        jsonData.put("result_type", "error");
        jsonData.put("result_body", message);

        return jsonData.toString();
    }

    /**
     * Создать ответ с типом "Успех".
     * @param request Запрос.
     * @param message Сообщение об ошибке.
     */
    static String createOk(final Request request, final String message) {
        JSONObject jsonData = new JSONObject();

        jsonData.put("request_id", request.id);
        jsonData.put("result_type", "ok");
        jsonData.put("result_body", message);

        return jsonData.toString();
    }
}
