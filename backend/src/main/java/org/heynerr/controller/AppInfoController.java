package org.heynerr.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/app-info")
public class AppInfoController {

    @Value("${app.version}")
    private String appVersion;

    @GetMapping
    public Map<String, String> getAppInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("version", appVersion);
        return info;
    }
}

