package com.remotefileexplorer.transport;

import org.json.simple.JSONObject;

/**
 * Ответ (Транспортный уровень абстракции).
 */
final class Response {
    private Response() {}

    /**
     * Создать ответ с типом "Ошибка".
     * @param body Содержимое ответа.
     */
    static String createError(final String body) {
        return createResponse(null, false, body);
    }

    /**
     * Создать ответ с типом "Ошибка".
     * @param request Запрос.
     * @param body Содержимое ответа.
     */
    static String createError(final Request request, final String body) {
        return createResponse(request.id, false, body);
    }

    /**
     * Создать ответ с типом "Успех".
     * @param request Запрос.
     * @param body Содержимое ответа.
     */
    static String createOk(final Request request, final String body) {
        return createResponse(request.id, true, body);
    }

    /**
     * Создать ответ.
     * @param request_id Идентификатор запроса.
     * @param success Флаг результата обработки.
     * @param body Содержимое ответа.
     */
    private static String createResponse(String request_id, boolean success, String body) {
        JSONObject jsonData = new JSONObject();

        if (request_id != null)
            jsonData.put("request_id", request_id);

        jsonData.put("result_type", success ? "ok" : "error");
        jsonData.put("result_body", body);

        return jsonData.toString();
    }
}