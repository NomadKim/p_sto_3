package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ali Veliev 10.12.2021
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {

    private Long id;
    private String name;
    private String description;

}
