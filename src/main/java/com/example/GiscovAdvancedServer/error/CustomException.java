package com.example.GiscovAdvancedServer.error;

import com.example.GiscovAdvancedServer.constans.ServerErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private ServerErrorCodes error;
}
