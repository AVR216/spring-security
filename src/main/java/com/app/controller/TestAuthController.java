package com.app.controller;

import com.app.controller.constants.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.TEST_RESOURCE)
public class TestAuthController {

    @GetMapping(path = {"/get"})
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
    public String helloPatch() {
        return "Hello world - Patch";
    }
}
