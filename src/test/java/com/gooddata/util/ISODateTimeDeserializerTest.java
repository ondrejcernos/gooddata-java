package com.gooddata.util;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ISODateTimeDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserialize() throws Exception {
        final DateTime expected = new DateTime(2012, 3, 20, 14, 31, 5, 3, DateTimeZone.UTC);
        final String json = MAPPER.writeValueAsString(new ISODateClass(expected));

        final JsonNode node = MAPPER.readTree(json);
        assertThat(node.path("date").getTextValue(), is("2012-03-20T14:31:05.003Z"));

        final ISODateClass moo = MAPPER.readValue(json, ISODateClass.class);
        assertThat(moo.getDate(), is(expected));
    }
}