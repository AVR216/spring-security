package com.app.controller;

import com.app.controller.constants.RestConstants;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.RESOURCE)
//@PreAuthorize("denyAll()")
public class TestAuthController {

    @GetMapping(path = {"/get"})
    //@PreAuthorize("hasAuthority('READ')")
    public String helloGet() {
        return "Hello world - get";
    }

    @PostMapping(path = {"/post"})
    public String helloPost() {
        return "Hello world - post";
    }
    @PutMapping(path = {"/put"})
    public String helloPut() {
        return "Hello world - put";
    }
    @DeleteMapping(path = {"/put"})
    public String helloDelete() {
        return "Hello world - delete";
    }
    @PatchMapping(path = {"/patch"})
    //@PreAuthorize("hasAuthority('REFACTOR')")
    public String helloPatch() {
        return "Hello world - Patch";
    }
}
