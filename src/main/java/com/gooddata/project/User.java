package com.gooddata.project;

import com.gooddata.md.Meta;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.web.util.UriTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User.
 */
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("user")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {

    private UserContent content;

    @JsonCreator
    User(@JsonProperty("content") final UserContent content) {
        this.content = content;
    }

    public String getEmail() {
        return content.getEmail();
    }

    public String getStatus() {
        return content.getStatus();
    }

    public String getLastName() {
        return content.getLastName();
    }

    public List<String> getUserRoles() {
        return content.getUserRoles();
    }

    public String getLogin() {
        return content.getLogin();
    }

    public String getFirstName() {
        return content.getFirstName();
    }

    public String getPhoneNumber() {
        return content.getPhoneNumber();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private static class UserContent {

        @JsonProperty("email")
        private String email;

        @JsonProperty("firstname")
        private String firstName;

        @JsonProperty("userRoles")
        private List<String> userRoles;

        @JsonProperty("phonenumber")
        private String phoneNumber;

        @JsonProperty("status")
        private String status;

        @JsonProperty("lastname")
        private String lastName;

        @JsonProperty("login")
        private String login;

        @JsonCreator
        public UserContent(@JsonProperty("email") final String email,
                           @JsonProperty("firstname") final String firstName,
                           @JsonProperty("userRoles") final List<String> userRoles,
                           @JsonProperty("phonenumber") final String phoneNumber,
                           @JsonProperty("status") final String status,
                           @JsonProperty("lastname") final String lastName,
                           @JsonProperty("login") final String login) {
            this.email = email;
            this.firstName = firstName;
            this.userRoles = userRoles;
            this.phoneNumber = phoneNumber;
            this.status = status;
            this.lastName = lastName;
            this.login = login;
        }

        public String getEmail() {
            return email;
        }

        public String getFirstName() {
            return firstName;
        }

        public List<String> getUserRoles() {
            return userRoles;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getStatus() {
            return status;
        }

        public String getLastName() {
            return lastName;
        }

        public String getLogin() {
            return login;
        }
    }
}
