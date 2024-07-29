package com.example.proserver.error;

import com.example.proserver.constans.ServerErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private ServerErrorCodes error;
}
