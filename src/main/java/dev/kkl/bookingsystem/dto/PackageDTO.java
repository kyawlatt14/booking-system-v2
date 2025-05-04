package dev.kkl.bookingsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageDTO {
    private Long id;
    private String title;
    private String description;
    private String country;
    private int totalCredits;
    private double price;
    private int validityDays;
}
