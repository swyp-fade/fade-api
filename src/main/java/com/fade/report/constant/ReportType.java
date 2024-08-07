package com.fade.report.constant;

import lombok.Getter;

@Getter
public enum ReportType {
    PORNO_OR_SEXUAL_IMAGE("PORNO_OR_SEXUAL_IMAGE"),
    ILLEGAL_USE_OR_AI_IMAGE("ILLEGAL_USE_OR_AI_IMAGE"),
    UNRELATED_OR_SPAM("UNRELATED_OR_SPAM"),
    REPUGNANT_SYMBOL("REPUGNANT_SYMBOL"),
    OTHER("OTHER");

    private final String type;

    ReportType(String type){
        this.type = type;
    }
}
