package com.chatebook.common.common;

import com.chatebook.common.constant.CommonConstant;
import com.chatebook.common.payload.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Slf4j
public final class CommonFunction {

    private static final String ERROR_FILE = "errors.yml";

    private static final String VALIDATION_FILE = "validations.yml";

    CommonFunction() {}

    /**
     * Extract current date time
     *
     * @return Timestamp
     */
    public static Timestamp getCurrentDateTime() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    /**
     * Extract exception error
     *
     * @param error String error
     * @return ErrorResponse
     */
    public static ErrorResponse getExceptionError(String error) {
        ReadYAML readYAML = new ReadYAML();
        Map<String, Object> errors = readYAML.getValueFromYAML(ERROR_FILE);
        Map<String, Object> objError = (Map<String, Object>) errors.get(error);
        String code = (String) objError.get("code");
        String message = (String) objError.get("message");
        return new ErrorResponse(code, message);
    }

    /**
     * Extract validation error
     *
     * @param resource String resource
     * @param fieldName String fieldName
     * @param validation String validation
     * @return ErrorResponse
     */
    public static ErrorResponse getValidationError(
            String resource, String fieldName, String validation) {
        if (fieldName.contains("[")) {
            fieldName = handleFieldName(fieldName);
        }

        ReadYAML readYAML = new ReadYAML();
        Map<String, Object> errors = readYAML.getValueFromYAML(VALIDATION_FILE);
        Map<String, Object> fields = (Map<String, Object>) errors.get(resource);
        Map<String, Object> objErrors = (Map<String, Object>) fields.get(fieldName);
        Map<String, Object> objError = (Map<String, Object>) objErrors.get(validation);
        String code = (String) objError.get("code");
        String message = (String) objError.get("message");
        return new ErrorResponse(code, message);
    }

    /**
     * Handle field name
     *
     * @param fieldName String fielaName
     * @return String
     */
    public static String handleFieldName(String fieldName) {
        String index = fieldName.substring(fieldName.indexOf("[") + 1, fieldName.indexOf("]"));
        return fieldName.replaceAll(index, "");
    }

    /**
     * Parse object to json string
     *
     * @param ob {@link Object} to parse
     * @return String
     */
    public static String convertToJSONString(Object ob) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(ob);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Convert camel case to snake case
     *
     * @param input string type camel case
     * @return String type snake case
     */
    public static String convertToSnakeCase(String input) {
        return input.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static String getSubRole(String role) {
        return role.replace(CommonConstant.ROLE_PREFIX, "");
    }
}

