package com.agleveratto.bitcoin.exceptions;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponse {

    private List<Error> errors = new ArrayList<>();
}
