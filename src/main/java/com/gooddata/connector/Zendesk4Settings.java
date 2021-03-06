/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.connector;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import static com.gooddata.util.Validate.notEmpty;
import static com.gooddata.connector.ConnectorType.ZENDESK4;

/**
 * Zendesk 4 (Insights) connector settings.
 */
public class Zendesk4Settings implements Settings {

    private String apiUrl;
    private final String type;
    private final String syncTime;
    private final String syncTimeZone;

    public Zendesk4Settings(final String apiUrl) {
        this(apiUrl, null, null, null);
    }

    @JsonCreator
    public Zendesk4Settings(@JsonProperty("apiUrl") String apiUrl, @JsonProperty("type") String type,
                            @JsonProperty("syncTime") String syncTime,
                            @JsonProperty("syncTimeZone") String syncTimeZone) {
        this.apiUrl = notEmpty(apiUrl, "apiUrl");
        this.type = type;
        this.syncTime = syncTime;
        this.syncTimeZone = syncTimeZone;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(final String apiUrl) {
        this.apiUrl = notEmpty(apiUrl, "apiUrl");
    }

    public String getType() {
        return type;
    }

    public String getSyncTime() {
        return syncTime;
    }

    public String getSyncTimeZone() {
        return syncTimeZone;
    }

    @Override
    public ConnectorType getConnectorType() {
        return ZENDESK4;
    }

    /**
     * Type of Zendesk account.
     */
    public enum Zendesk4Type {plus, enterprise}
}
