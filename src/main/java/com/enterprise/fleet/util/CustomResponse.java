package com.enterprise.fleet.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse {
    private Boolean status;
    private String message;
    private Object data;
}
