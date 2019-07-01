package com.demo.account.accountapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The model Client class that will be used in serde and will represent a client in the application.
 *
 * @author Sagar Bijlwan
 */
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Client {

    @JsonProperty("client_id")
    private final Long id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("surname")
    private final String surname;

    @JsonProperty("primary_address")
    private final String primaryAddress;

    @JsonProperty("secondary_address")
    private final String secondaryAddress;


   // TODO : Add AccountId Set in case we would like to display accountId in response.

}
