package com.udacity.jdnd.course3.critter.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Object Not Found")
public class ObjectNotFoundException extends Exception {

}

