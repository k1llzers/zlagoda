package org.naukma.zlagoda;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UiController {
    @RequestMapping(value = "/{path:[^.]*}")
    public String redirectSingle() {
        return "forward:/";
    }

    @GetMapping("/*/{path:[^.]*}")
    public String redirectNested() {
        return "forward:/";
    }
}
